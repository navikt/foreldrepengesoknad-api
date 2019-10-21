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
        LOG.info("Bruker storage " + storage.getClass().getSimpleName());
    }

    public Optional<String> hentSøknad() {
        String fnr = tokenHelper.autentisertBruker();
        String directory = crypto.encryptDirectoryName(fnr);
        LOG.info("Henter søknad fra katalog {}", directory);
        Optional<String> søknad = storage.getTmp(directory, SØKNAD)
                .map(s -> crypto.decrypt(s, fnr));
        LOG.info("Hentet søknad fra katalog {}", directory);
        return søknad;

    }

    public void lagreSøknad(String søknad) {
        String fnr = tokenHelper.autentisertBruker();
        String directory = crypto.encryptDirectoryName(fnr);
        LOG.info("Skriver søknad til katalog {}", directory);
        String encryptedValue = crypto.encrypt(søknad, fnr);
        storage.putTmp(directory, SØKNAD, encryptedValue);
        LOG.info("Skrev søknad til katalog {}", directory);
    }

    public void slettSøknad() {
        String fnr = tokenHelper.autentisertBruker();
        String directory = crypto.encryptDirectoryName(fnr);
        LOG.info("Fjerner søknad fra katalog {}", directory);
        storage.deleteTmp(directory, SØKNAD);
        LOG.info("Fjernet søknad fra katalog {}", directory);
    }

    public Optional<Attachment> hentVedlegg(String key) {
        String fnr = tokenHelper.autentisertBruker();
        String directory = crypto.encryptDirectoryName(fnr);
        LOG.info("Henter vedlegg med nøkkel {} fra katalog {}", key, directory);
        Optional<Attachment> v = storage.getTmp(directory, key)
                .map(vedlegg -> crypto.decrypt(vedlegg, fnr))
                .map(Attachment::fromJson);
        LOG.info("Hentet vedlegg med nøkkel {} fra katalog {}", key, directory);
        return v;
    }

    public void lagreVedlegg(Attachment attachment) {
        sjekker.sjekkAttachments(attachment);
        String fnr = tokenHelper.autentisertBruker();
        String directory = crypto.encryptDirectoryName(fnr);
        LOG.info("Skriver vedlegg {} til katalog {}", attachment, directory);
        String encryptedValue = crypto.encrypt(attachment.toJson(), fnr);
        storage.putTmp(directory, attachment.uuid, encryptedValue);
        LOG.info("Skrev vedlegg {} til katalog {}", attachment, directory);
    }

    public void slettVedlegg(Vedlegg vedlegg) {
        if (vedlegg.getUrl() != null) {
            slettVedlegg(vedlegg.getUuid());
        }
    }

    public void slettVedlegg(String uuid) {
        if (uuid != null) {
            String directory = crypto.encryptDirectoryName(tokenHelper.autentisertBruker());
            LOG.info("Fjerner vedlegg med nøkkel {} fra katalog {}", uuid, directory);
            storage.deleteTmp(directory, uuid);
            LOG.info("Fjernet vedlegg med nøkkel {} fra katalog {}", uuid, directory);
        }
    }

    public Optional<String> hentKvittering(String type) {
        String fnr = tokenHelper.autentisertBruker();
        String directory = crypto.encryptDirectoryName(fnr);
        LOG.info("Henter kvittering fra katalog {}", directory);
        Optional<String> kvittering = storage.get(directory, type)
                .map(k -> crypto.decrypt(k, fnr));
        LOG.info("Hentet kvittering fra katalog {}", directory);
        return kvittering;
    }

    public void lagreKvittering(String type, String kvittering) {
        String fnr = tokenHelper.autentisertBruker();
        String directory = crypto.encryptDirectoryName(fnr);
        LOG.info("Skriver kvittering til katalog {}", directory);
        String encryptedValue = crypto.encrypt(kvittering, fnr);
        storage.put(directory, type, encryptedValue);
        LOG.info("Skrev kvittering til katalog {}", directory);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[tokenHelper=" + tokenHelper + ", storage=" + storage + ", crypto="
                + crypto + ", sjekker=" + sjekker + "]";
    }
}
