package no.nav.foreldrepenger.selvbetjening.stub;

import no.nav.foreldrepenger.selvbetjening.felles.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageStub implements Storage {

    private static final Logger log = LoggerFactory.getLogger(StorageStub.class);

    @Override
    public void put(String key, String value) {
        log.debug("Would have stored: " + key + " -> " + value);
    }

    @Override
    public String get(String key) {
        return "r6pbUjCtjD8XvzxtQnkN9w==";
    }

    @Override
    public void delete(String key) {
        log.debug("Would have deleted: " + key);
    }
}
