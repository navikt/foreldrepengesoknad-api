package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static no.nav.foreldrepenger.selvbetjening.vedlegg.DelegerendeVedleggSjekker.DELEGERENDE;
import static no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter.megabytes;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekker;

@Service
public class KryptertMellomlagring {
    private static final Logger LOG = LoggerFactory.getLogger(KryptertMellomlagring.class);
    private static final String SØKNAD = "soknad";
    private static final Gson GSON = new Gson();
    private final Mellomlagring mellomlagring;
    private final MellomlagringKrypto krypto;
    private final VedleggSjekker sjekker;

    public KryptertMellomlagring(Mellomlagring mellomlagring,
                                 MellomlagringKrypto krypto,
                                 @Qualifier(DELEGERENDE) VedleggSjekker sjekker) {
        this.mellomlagring = mellomlagring;
        this.krypto = krypto;
        this.sjekker = sjekker;
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

    public Optional<Attachment> lesKryptertVedlegg(String key, Ytelse ytelse) {
        return mellomlagring.les(ytelsespesifikkMappe(ytelse), key)
            .map(krypto::decrypt)
            .map(v -> GSON.fromJson(v, Attachment.class));
    }

    public void lagreKryptertVedlegg(Attachment vedlegg, Ytelse ytelse) {
        LOG.info("Mellomlagrer vedlegg med opprinnelig PDF størrelse lik {}MB", megabytes(vedlegg.bytes));
        sjekker.sjekk(vedlegg);
        mellomlagring.lagre(ytelsespesifikkMappe(ytelse), vedlegg.getUuid(), krypto.encrypt(GSON.toJson(vedlegg)));
    }

    public void slettKryptertVedlegg(String uuid, Ytelse ytelse) {
        if (uuid != null) {
            mellomlagring.slett(ytelsespesifikkMappe(ytelse), uuid);
        }
    }

    public void slettMellomlagring(Ytelse ytelse) {
        mellomlagring.slettAll(ytelsespesifikkMappe(ytelse));
    }

    private String ytelsespesifikkMappe(Ytelse ytelse) {
        return krypto.mappenavn() + "/" + ytelse.name() + "/";
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[storage=" + mellomlagring + ", crypto=" + krypto + ", sjekker=" + sjekker + "]";
    }
}
