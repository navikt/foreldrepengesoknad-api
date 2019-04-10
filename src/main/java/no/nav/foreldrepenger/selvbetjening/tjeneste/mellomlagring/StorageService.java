package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;

@Service
public class StorageService {
    private static final Logger LOG = getLogger(StorageService.class);

    private static final String SØKNAD = "soknad";

    private final TokenUtil tokenHelper;
    private final Storage storage;
    private final StorageCrypto crypto;

    public StorageService(TokenUtil tokenHelper, Storage storage, StorageCrypto crypto) {
        this.tokenHelper = tokenHelper;
        this.storage = storage;
        this.crypto = crypto;
    }

    public Optional<String> hentSøknad() {
        String fnr = tokenHelper.autentisertBruker();
        String directory = crypto.encryptDirectoryName(fnr);
        LOG.trace("Henter søknad fra katalog {}", directory);
        return storage.getTmp(directory, SØKNAD)
                .map(søknad -> crypto.decrypt(søknad, fnr));
    }

    public void lagreSøknad(String søknad) {
        String fnr = tokenHelper.autentisertBruker();
        String directory = crypto.encryptDirectoryName(fnr);
        LOG.trace("Skriver søknad til katalog {}", directory);
        String encryptedValue = crypto.encrypt(søknad, fnr);
        storage.putTmp(directory, SØKNAD, encryptedValue);
    }

    public void slettSøknad() {
        String fnr = tokenHelper.autentisertBruker();
        String directory = crypto.encryptDirectoryName(fnr);
        LOG.info("Fjerner søknad fra katalog {}", directory);
        storage.deleteTmp(directory, SØKNAD);
    }

    public Optional<Attachment> hentVedlegg(String key) {
        String fnr = tokenHelper.autentisertBruker();
        String directory = crypto.encryptDirectoryName(fnr);
        LOG.info("Henter vedlegg med nøkkel {} fra katalog {}", key, directory);
        return storage.getTmp(directory, key)
                .map(vedlegg -> crypto.decrypt(vedlegg, fnr))
                .map(Attachment::fromJson);
    }

    public void lagreVedlegg(Attachment attachment) {
        String fnr = tokenHelper.autentisertBruker();
        String directory = crypto.encryptDirectoryName(fnr);
        LOG.info("Skriver vedlegg {} til katalog {}", attachment, directory);
        String encryptedValue = crypto.encrypt(attachment.toJson(), fnr);
        storage.putTmp(directory, attachment.uuid, encryptedValue);
    }

    public void slettVedlegg(String key) {
        String directory = crypto.encryptDirectoryName(tokenHelper.autentisertBruker());
        LOG.info("Fjerner vedlegg med nøkkel {} fra katalog {}", key, directory);
        storage.deleteTmp(directory, key);
    }

    public Optional<String> hentKvittering(String type) {
        String fnr = tokenHelper.autentisertBruker();
        String directory = crypto.encryptDirectoryName(fnr);
        LOG.trace("Henter kvittering fra katalog {}", directory);
        return storage.get(directory, type)
                .map(kvittering -> crypto.decrypt(kvittering, fnr));
    }

    public void lagreKvittering(String type, String kvittering) {
        String fnr = tokenHelper.autentisertBruker();
        String directory = crypto.encryptDirectoryName(fnr);
        LOG.trace("Skriver kvittering til katalog {}", directory);
        String encryptedValue = crypto.encrypt(kvittering, fnr);
        storage.put(directory, type, encryptedValue);
    }
}
