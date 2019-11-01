package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekker;

@Service
public class KryptertMellomlagringTjeneste {
    private static final Logger LOG = getLogger(KryptertMellomlagringTjeneste.class);

    private static final String SØKNAD = "soknad";

    private final TokenUtil tokenHelper;
    private final MellomlagringTjeneste mellomlagring;
    private final MellomlagringKrypto krypto;
    private final VedleggSjekker sjekker;

    public KryptertMellomlagringTjeneste(TokenUtil tokenHelper, MellomlagringTjeneste mellomlagring,
            MellomlagringKrypto krypto, VedleggSjekker sjekker) {
        this.tokenHelper = tokenHelper;
        this.mellomlagring = mellomlagring;
        this.krypto = krypto;
        this.sjekker = sjekker;
    }

    public Optional<String> hentSøknad() {
        String fnr = tokenHelper.autentisertBruker();
        return mellomlagring.getTmp(krypto.katalognavn(fnr), SØKNAD)
                .map(s -> krypto.decrypt(s, fnr));
    }

    public void lagreSøknad(String søknad) {
        String fnr = tokenHelper.autentisertBruker();
        mellomlagring.putTmp(krypto.katalognavn(fnr), SØKNAD, krypto.encrypt(søknad, fnr));
    }

    public void slettSøknad() {
        mellomlagring.deleteTmp(krypto.katalognavn(tokenHelper.autentisertBruker()), SØKNAD);
    }

    public Optional<Attachment> hentVedlegg(String key) {
        String fnr = tokenHelper.autentisertBruker();
        return mellomlagring.getTmp(krypto.katalognavn(fnr), key)
                .map(vedlegg -> krypto.decrypt(vedlegg, fnr))
                .map(Attachment::fromJson);
    }

    public void lagreVedlegg(Attachment attachment) {
        sjekker.sjekkAttachments(attachment);
        String fnr = tokenHelper.autentisertBruker();
        mellomlagring.putTmp(krypto.katalognavn(fnr), attachment.uuid,
                krypto.encrypt(attachment.toJson(), fnr));
    }

    public void slettVedlegg(Vedlegg vedlegg) {
        if (vedlegg.getUrl() != null) {
            slettVedlegg(vedlegg.getUuid());
        }
    }

    public void slettVedlegg(String uuid) {
        if (uuid != null) {
            mellomlagring.deleteTmp(krypto.katalognavn(tokenHelper.autentisertBruker()), uuid);
        }
    }

    public Optional<String> hentKvittering(String type) {
        String fnr = tokenHelper.autentisertBruker();
        return mellomlagring.get(krypto.katalognavn(fnr), type)
                .map(k -> krypto.decrypt(k, fnr));
    }

    public void lagreKvittering(String type, String kvittering) {
        String fnr = tokenHelper.autentisertBruker();
        mellomlagring.put(krypto.katalognavn(fnr), type, krypto.encrypt(kvittering, fnr));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[tokenHelper=" + tokenHelper + ", storage=" + mellomlagring + ", crypto="
                + krypto + ", sjekker=" + sjekker + "]";
    }
}
