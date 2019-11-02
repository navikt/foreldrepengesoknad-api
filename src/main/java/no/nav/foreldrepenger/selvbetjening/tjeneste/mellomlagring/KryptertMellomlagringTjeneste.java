package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekker;

@Service
public class KryptertMellomlagringTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(KryptertMellomlagringTjeneste.class);
    private static final String SØKNAD = "soknad";
    private static final Gson GSON = new Gson();
    private final TokenUtil tokenUtil;
    private final MellomlagringTjeneste mellomlagring;
    private final MellomlagringKrypto krypto;
    private final VedleggSjekker sjekker;

    public KryptertMellomlagringTjeneste(TokenUtil tokenUtil, MellomlagringTjeneste mellomlagring,
            MellomlagringKrypto krypto, VedleggSjekker sjekker) {
        this.tokenUtil = tokenUtil;
        this.mellomlagring = mellomlagring;
        this.krypto = krypto;
        this.sjekker = sjekker;
    }

    public Optional<String> lesKryptertSøknad() {
        LOG.info("Leser kryptert søknad");
        String fnr = tokenUtil.autentisertBruker();
        return mellomlagring.lesTmp(krypto.katalognavn(fnr), SØKNAD)
                .map(s -> krypto.decrypt(s, fnr));
    }

    public void lagreKryptertSøknad(String søknad) {
        LOG.info("Lagrer kryptert søknad");
        String fnr = tokenUtil.autentisertBruker();
        mellomlagring.lagreTmp(krypto.katalognavn(fnr), SØKNAD, krypto.encrypt(søknad, fnr));
    }

    public void slettKryptertSøknad() {
        LOG.info("Sletter kryptert søknad");
        mellomlagring.slettTmp(krypto.katalognavn(tokenUtil.autentisertBruker()), SØKNAD);
    }

    public Optional<Attachment> lesKryptertVedlegg(String key) {
        LOG.info("Leser kryptert vedlegg");
        String fnr = tokenUtil.autentisertBruker();
        return mellomlagring.lesTmp(krypto.katalognavn(fnr), key)
                .map(vedlegg -> krypto.decrypt(vedlegg, fnr))
                .map(v -> GSON.fromJson(v, Attachment.class));
    }

    public void lagreKryptertVedlegg(Attachment attachment) {
        LOG.info("Lagrer kryptert vedlegg");
        sjekker.sjekkAttachments(attachment);
        String fnr = tokenUtil.autentisertBruker();
        mellomlagring.lagreTmp(krypto.katalognavn(fnr), attachment.uuid,
                krypto.encrypt(GSON.toJson(attachment), fnr));
    }

    public void slettKryptertVedlegg(Vedlegg vedlegg) {
        if (vedlegg.getUrl() != null) {
            slettKryptertVedlegg(vedlegg.getUuid());
        }
    }

    public void slettKryptertVedlegg(String uuid) {
        if (uuid != null) {
            LOG.info("Sletter kryptert vedlegg");
            mellomlagring.slettTmp(krypto.katalognavn(tokenUtil.autentisertBruker()), uuid);
        }
    }

    public Optional<String> lesKryptertKvittering(String type) {
        LOG.info("Leser kryptert kvittering");
        String fnr = tokenUtil.autentisertBruker();
        return mellomlagring.les(krypto.katalognavn(fnr), type)
                .map(k -> krypto.decrypt(k, fnr));
    }

    public void lagreKryptertKvittering(String type, String kvittering) {
        LOG.info("Lagrer kryptert kvittering");
        String fnr = tokenUtil.autentisertBruker();
        mellomlagring.lagre(krypto.katalognavn(fnr), type, krypto.encrypt(kvittering, fnr));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[tokenUtil=" + tokenUtil + ", storage=" + mellomlagring + ", crypto="
                + krypto + ", sjekker=" + sjekker + "]";
    }
}
