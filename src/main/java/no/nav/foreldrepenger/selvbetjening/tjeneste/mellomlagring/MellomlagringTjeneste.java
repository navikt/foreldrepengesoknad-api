package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekker;

@Service
public class MellomlagringTjeneste {
    private static final Logger LOG = getLogger(MellomlagringTjeneste.class);

    private static final String SØKNAD = "soknad";

    private final TokenUtil tokenHelper;
    private final Storage storage;
    private final StorageCrypto crypto;
    private final VedleggSjekker sjekker;

    public MellomlagringTjeneste(TokenUtil tokenHelper, Storage storage, StorageCrypto crypto, VedleggSjekker sjekker) {
        this.tokenHelper = tokenHelper;
        this.storage = storage;
        this.crypto = crypto;
        this.sjekker = sjekker;
    }

    public Optional<String> hentSøknad() {
        String fnr = tokenHelper.autentisertBruker();
        return storage.getTmp(crypto.encryptDirectoryName(fnr), SØKNAD)
                .map(s -> crypto.decrypt(s, fnr));

    }

    public void lagreSøknad(String søknad) {
        String fnr = tokenHelper.autentisertBruker();
        storage.putTmp(crypto.encryptDirectoryName(fnr), SØKNAD, crypto.encrypt(søknad, fnr));
    }

    public void slettSøknad() {
        storage.deleteTmp(crypto.encryptDirectoryName(tokenHelper.autentisertBruker()), SØKNAD);
    }

    public Optional<Attachment> hentVedlegg(String key) {
        String fnr = tokenHelper.autentisertBruker();
        return storage.getTmp(crypto.encryptDirectoryName(fnr), key)
                .map(vedlegg -> crypto.decrypt(vedlegg, fnr))
                .map(Attachment::fromJson);
    }

    public void lagreVedlegg(Attachment attachment) {
        sjekker.sjekkAttachments(attachment);
        String fnr = tokenHelper.autentisertBruker();
        storage.putTmp(crypto.encryptDirectoryName(fnr), attachment.uuid, crypto.encrypt(attachment.toJson(), fnr));
    }

    public void slettVedlegg(Vedlegg vedlegg) {
        if (vedlegg.getUrl() != null) {
            slettVedlegg(vedlegg.getUuid());
        }
    }

    public void slettVedlegg(String uuid) {
        if (uuid != null) {
            storage.deleteTmp(crypto.encryptDirectoryName(tokenHelper.autentisertBruker()), uuid);
        }
    }

    public Optional<String> hentKvittering(String type) {
        String fnr = tokenHelper.autentisertBruker();
        return storage.get(crypto.encryptDirectoryName(fnr), type)
                .map(k -> crypto.decrypt(k, fnr));
    }

    public void lagreKvittering(String type, String kvittering) {
        String fnr = tokenHelper.autentisertBruker();
        storage.put(crypto.encryptDirectoryName(fnr), type, crypto.encrypt(kvittering, fnr));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[tokenHelper=" + tokenHelper + ", storage=" + storage + ", crypto="
                + crypto + ", sjekker=" + sjekker + "]";
    }
}
