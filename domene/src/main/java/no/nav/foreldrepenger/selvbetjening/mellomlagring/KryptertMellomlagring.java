package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static no.nav.foreldrepenger.selvbetjening.mellomlagring.MellomlagringType.KORTTIDS;
import static no.nav.foreldrepenger.selvbetjening.vedlegg.DelegerendeVedleggSjekker.DELEGERENDE;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekker;

@Service
public class KryptertMellomlagring {
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

    public Optional<String> lesKryptertSøknad(Ytelse ytelse) {
        return mellomlagring.les(KORTTIDS, katalog(), utledNøkkel(ytelse))
            .map(krypto::decrypt);
    }

    public void lagreKryptertSøknad(String søknad, Ytelse ytelse) {
        mellomlagring.lagre(KORTTIDS, katalog(), utledNøkkel(ytelse), krypto.encrypt(søknad));
    }

    public void slettKryptertSøknad(Ytelse ytelse) {
        mellomlagring.slett(KORTTIDS, katalog(), utledNøkkel(ytelse));
    }

    public Optional<Attachment> lesKryptertVedlegg(String key) {
        return mellomlagring.les(KORTTIDS, katalog(), key)
            .map(krypto::decrypt)
            .map(v -> GSON.fromJson(v, Attachment.class));
    }

    public void lagreKryptertVedlegg(Attachment vedlegg) {
        sjekker.sjekk(vedlegg);
        mellomlagring.lagre(KORTTIDS, katalog(), vedlegg.getUuid(), krypto.encrypt(GSON.toJson(vedlegg)));
    }

    public void slettKryptertVedlegg(String uuid) {
        if (uuid != null) {
            mellomlagring.slett(KORTTIDS, katalog(), uuid);
        }
    }

    private String katalog() {
        return krypto.katalognavn();
    }

    private static String utledNøkkel(Ytelse ytelse) {
        return switch (ytelse) {
            case FORELDREPENGER, SVANGERSKAPSPENGER, ENGANGSSTØNAD -> ytelse.name();
            case IKKE_OPPGITT -> SØKNAD;
        };
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[storage=" + mellomlagring + ", crypto=" + krypto + ", sjekker=" + sjekker + "]";
    }
}
