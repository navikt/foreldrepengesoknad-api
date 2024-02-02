package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class KryptertMellomlagring {
    private static final String SØKNAD = "soknad";
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
        mellomlagring.oppdaterMellomlagredeVedleggOgSøknad(ytelsespesifikkMappe(ytelse));
    }

    public Optional<byte[]> lesKryptertVedlegg(String key, Ytelse ytelse) {
        return mellomlagring.lesVedlegg(ytelsespesifikkMappeVedlegg(ytelse), key)
            .map(krypto::decryptVedlegg);
    }

    public void lagreKryptertVedlegg(Attachment vedlegg, Ytelse ytelse) {
        mellomlagring.lagreVedlegg(ytelsespesifikkMappeVedlegg(ytelse), vedlegg.getUuid(), krypto.encryptVedlegg(vedlegg.getBytes()));
    }

    public void slettMellomlagring(Ytelse ytelse) {
        mellomlagring.slettAll(ytelsespesifikkMappe(ytelse));
    }

    public void slettKryptertVedlegg(String uuid, Ytelse ytelse) {
        if (uuid != null) {
            mellomlagring.slett(ytelsespesifikkMappeVedlegg(ytelse), uuid);
        }
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
