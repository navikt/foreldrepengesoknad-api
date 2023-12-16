package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter.megabytes;

import java.math.RoundingMode;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class KryptertMellomlagring {
    private static final Logger LOG = LoggerFactory.getLogger(KryptertMellomlagring.class);
    private static final String SØKNAD = "soknad";
    private static final Gson GSON = new Gson(); // Deprecated
    private final Mellomlagring mellomlagring;
    private final MellomlagringKrypto krypto;

    public KryptertMellomlagring(Mellomlagring mellomlagring, MellomlagringKrypto krypto) {
        this.mellomlagring = mellomlagring;
        this.krypto = krypto;
    }

    public AktivMellomlagringDto finnesAktivMellomlagring() {
        var esEksisterer = mellomlagring.eksisterer(ytelsespesifikkMappe(Ytelse.ENGANGSSTONAD), SØKNAD);
        var fpEksisterer = mellomlagring.eksisterer(ytelsespesifikkMappe(Ytelse.FORELDREPENGER), SØKNAD);
        var svpEksisterer = mellomlagring.eksisterer(ytelsespesifikkMappe(Ytelse.SVANGERSKAPSPENGER), SØKNAD);
        return new AktivMellomlagringDto(esEksisterer, fpEksisterer, svpEksisterer);
    }

    public Optional<String> lesKryptertSøknad(Ytelse ytelse) {
        return mellomlagring.les(ytelsespesifikkMappe(ytelse), SØKNAD)
            .map(krypto::decrypt);
    }

    public void lagreKryptertSøknad(String søknad, Ytelse ytelse) {
        mellomlagring.lagre(ytelsespesifikkMappe(ytelse), SØKNAD, krypto.encrypt(søknad));
    }

    public void slettKryptertSøknad(Ytelse ytelse) {
        mellomlagring.slett(ytelsespesifikkMappe(ytelse), SØKNAD);
    }

    public Optional<byte[]> lesKryptertVedlegg(String key, Ytelse ytelse) {
        var gammel =  mellomlagring.les(ytelsespesifikkMappe(ytelse), key)
            .map(krypto::decrypt)
            .map(v -> GSON.fromJson(v, Attachment.class))
            .map(Attachment::getBytes);
        if (gammel.isPresent()) {
            LOG.info("Fant vedlegg mellomlagret på gammel format. Lagrer på nytt format.");
            mellomlagring.lagreVedlegg(ytelsespesifikkMappeVedlegg(ytelse), key, krypto.encryptVedlegg(gammel.get()));
            mellomlagring.slett(ytelsespesifikkMappe(ytelse), key);
            return gammel;
        }

        return mellomlagring.lesVedlegg(ytelsespesifikkMappeVedlegg(ytelse), key)
            .map(krypto::decryptVedlegg);
    }

    public void lagreKryptertVedlegg(Attachment vedlegg, Ytelse ytelse) {
        var kryptertVedlegg = krypto.encryptVedlegg(vedlegg.getBytes());
        mellomlagring.lagreVedlegg(ytelsespesifikkMappeVedlegg(ytelse), vedlegg.getUuid(), kryptertVedlegg);

        var opprinneligStørrelse = megabytes(vedlegg.bytes);
        var kryptert = megabytes(kryptertVedlegg);
        try {
            var økning = kryptert.divide(opprinneligStørrelse, 2, RoundingMode.HALF_UP);
            LOG.info("Mellomlagret vedlegg med opprinnelig PDF størrelse lik {}MB og endelig størrelse lik {}MB (x{})", opprinneligStørrelse, kryptert, økning);
        } catch (Exception e) {
            LOG.info("Mellomlagret vedlegg med opprinnelig PDF størrelse lik {}MB og endelig størrelse lik {}MB", opprinneligStørrelse, kryptert);
        }
    }

    public void slettKryptertVedlegg(String uuid, Ytelse ytelse) {
        if (uuid != null) {
            mellomlagring.slett(ytelsespesifikkMappe(ytelse), uuid);
        }
    }

    public void slettMellomlagring(Ytelse ytelse) {
        mellomlagring.slettAll(ytelsespesifikkMappe(ytelse));
    }

    protected String ytelsespesifikkMappe(Ytelse ytelse) {
        return krypto.mappenavn() + "/" + ytelse.name() + "/";
    }

    private String ytelsespesifikkMappeVedlegg(Ytelse ytelse) {
        return ytelsespesifikkMappe(ytelse) + "vedlegg/";
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[storage=" + mellomlagring + ", crypto=" + krypto + "]";
    }
}
