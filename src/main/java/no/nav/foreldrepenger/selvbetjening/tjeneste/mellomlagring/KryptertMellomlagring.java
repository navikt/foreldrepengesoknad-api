package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.MellomlagringType.KORTTIDS;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.MellomlagringType.LANGTIDS;
import static no.nav.foreldrepenger.selvbetjening.util.MDCUtil.CONFIDENTIAL;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekker;

@Service
public class KryptertMellomlagring {

    private static final Logger LOG = LoggerFactory.getLogger(KryptertMellomlagring.class);
    private static final String SØKNAD = "soknad";
    private static final Gson GSON = new Gson();
    private final Mellomlagring mellomlagring;
    private final MellomlagringKrypto krypto;
    private final VedleggSjekker sjekker;

    public KryptertMellomlagring(Mellomlagring mellomlagring, MellomlagringKrypto krypto, VedleggSjekker sjekker) {
        this.mellomlagring = mellomlagring;
        this.krypto = krypto;
        this.sjekker = sjekker;
    }

    public Optional<String> lesKryptertSøknad() {
        LOG.trace("Leser kryptert søknad fra {}", katalog());
        var søknad = mellomlagring.les(KORTTIDS, katalog(), SØKNAD)
                .map(krypto::decrypt);
        if (søknad.isPresent()) {
            LOG.trace("Lest kryptert søknad fra {}", katalog());
            LOG.info(CONFIDENTIAL, "Dekryptert søknad {}", søknad.get());
        } else {
            LOG.trace("Fant ingen kryptert søknad i {}", katalog());
        }
        return søknad;
    }

    public void lagreKryptertSøknad(String søknad) {
        LOG.trace("Lagrer kryptert søknad i {}", katalog());
        mellomlagring.lagre(KORTTIDS, katalog(), SØKNAD, krypto.encrypt(søknad));
        LOG.trace("Lagret kryptert søknad i {}", katalog());
    }

    public void slettKryptertSøknad() {
        LOG.trace("Sletter kryptert søknad fra {}", katalog());
        mellomlagring.slett(KORTTIDS, katalog(), SØKNAD);
        LOG.trace("Slettet kryptert søknad fra {}", katalog());
    }

    public Optional<Attachment> lesKryptertVedlegg(String key) {
        LOG.trace("Leser kryptert vedlegg fra {}", katalog());
        var vedlegg = mellomlagring.les(KORTTIDS, katalog(), key)
                .map(krypto::decrypt)
                .map(v -> GSON.fromJson(v, Attachment.class));
        if (vedlegg.isPresent()) {
            LOG.trace("Lest kryptert vedlegg");
            LOG.info(CONFIDENTIAL, "Dekryptert vedlegg {}", vedlegg.get());
        } else {
            LOG.trace("Fant intet kryptert vedlegg i {}", katalog());
        }
        return vedlegg;
    }

    public void lagreKryptertVedlegg(Attachment vedlegg) {
        LOG.trace("Lagrer kryptert vedlegg i {}", katalog());
        sjekker.sjekkAttachments(vedlegg);
        mellomlagring.lagre(KORTTIDS, katalog(), vedlegg.getUuid(), krypto.encrypt(GSON.toJson(vedlegg)));
        LOG.info("Lagret kryptert vedlegg i {}", katalog());
    }

    public void slettKryptertVedlegg(Vedlegg vedlegg) {
        if (vedlegg.getUrl() != null) {
            slettKryptertVedlegg(vedlegg.getUuid());
        }
    }

    public void slettKryptertVedlegg(String uuid) {
        if (uuid != null) {
            LOG.trace("Sletter kryptert vedlegg med uuid {} fra {}", uuid, katalog());
            mellomlagring.slett(KORTTIDS, katalog(), uuid);
            LOG.trace("Slettet kryptert vedlegg med uuid {} fra {}", uuid, katalog());
        }
    }

    public Optional<String> lesKryptertKvittering(String type) {
        LOG.trace("Leser kryptert kvittering fra {}", katalog());
        var kvittering = mellomlagring.les(LANGTIDS, katalog(), type)
                .map(krypto::decrypt);
        if (kvittering.isPresent()) {
            LOG.trace("Lest kryptert kvittering fra {}", katalog());
            LOG.info(CONFIDENTIAL, "Dekryptert kvittering {}", kvittering.get());
        } else {
            LOG.trace("Fant ingen kryptert kvittering i {}", katalog());
        }
        return kvittering;
    }

    public void lagreKryptertKvittering(String type, String kvittering) {
        LOG.trace("Lagrer kryptert kvittering i {}", katalog());
        mellomlagring.lagre(KORTTIDS, katalog(), type, krypto.encrypt(kvittering));
        LOG.trace("Lagret kryptert kvittering i katalog {}", katalog());
    }

    private String katalog() {
        return krypto.katalognavn();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[storage=" + mellomlagring + ", crypto=" + krypto + ", sjekker=" + sjekker
                + "]";
    }
}
