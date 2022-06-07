package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static no.nav.boot.conditionals.EnvUtil.CONFIDENTIAL;
import static no.nav.foreldrepenger.selvbetjening.mellomlagring.MellomlagringType.KORTTIDS;
import static no.nav.foreldrepenger.selvbetjening.vedlegg.DelegerendeVedleggSjekker.DELEGERENDE;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.VedleggFrontend;
import no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekker;

@Service
public class KryptertMellomlagring {

    private static final Logger LOG = LoggerFactory.getLogger(KryptertMellomlagring.class);
    private static final String SØKNAD = "soknad";
    private static final Gson GSON = new Gson();
    private final Mellomlagring mellomlagring;
    private final MellomlagringKrypto krypto;
    private final VedleggSjekker sjekker;

    public KryptertMellomlagring(Mellomlagring mellomlagring, MellomlagringKrypto krypto,
                                 @Qualifier(DELEGERENDE) VedleggSjekker sjekker) {
        this.mellomlagring = mellomlagring;
        this.krypto = krypto;
        this.sjekker = sjekker;
    }

    public Optional<String> lesKryptertSøknad() {
        var søknad = mellomlagring.les(KORTTIDS, katalog(), SØKNAD)
            .map(krypto::decrypt);
        if (søknad.isPresent()) {
            LOG.info(CONFIDENTIAL, "Dekryptert søknad {}", søknad.get());
        }
        return søknad;
    }

    public void lagreKryptertSøknad(String søknad) {
        mellomlagring.lagre(KORTTIDS, katalog(), SØKNAD, krypto.encrypt(søknad));
    }

    public void slettKryptertSøknad() {
        mellomlagring.slett(KORTTIDS, katalog(), SØKNAD);
    }

    public Optional<Attachment> lesKryptertVedlegg(String key) {
        var vedlegg = mellomlagring.les(KORTTIDS, katalog(), key)
            .map(krypto::decrypt)
            .map(v -> GSON.fromJson(v, Attachment.class));
        if (vedlegg.isPresent()) {
            LOG.info(CONFIDENTIAL, "Dekryptert vedlegg {}", vedlegg.get());
        }
        return vedlegg;
    }

    public void lagreKryptertVedlegg(Attachment vedlegg) {
        sjekker.sjekk(vedlegg);
        mellomlagring.lagre(KORTTIDS, katalog(), vedlegg.getUuid(), krypto.encrypt(GSON.toJson(vedlegg)));
    }

    public void slettKryptertVedlegg(VedleggFrontend vedlegg) {
        if (vedlegg.getUrl() != null) {
            slettKryptertVedlegg(vedlegg.getUuid());
        }
    }

    public void slettKryptertVedlegg(String uuid) {
        if (uuid != null) {
            mellomlagring.slett(KORTTIDS, katalog(), uuid);
        }
    }

    private String katalog() {
        return krypto.katalognavn();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[storage=" + mellomlagring + ", crypto=" + krypto + ", sjekker=" + sjekker + "]";
    }
}
