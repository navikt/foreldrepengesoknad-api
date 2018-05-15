package no.nav.foreldrepenger.selvbetjening.stub;

import no.nav.foreldrepenger.selvbetjening.felles.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class StorageStub implements Storage {

    private static final Logger log = LoggerFactory.getLogger(StorageStub.class);

    @Override
    public void put(String directory, String key, String value) {
        log.info("Would have stored: " + key + " -> " + value + " in directory " + directory);
    }

    @Override
    public Optional<String> get(String directory, String key) {
        return Optional.of("r6pbUjCtjD8XvzxtQnkN9w==");
    }

    @Override
    public void delete(String directory, String key) {
        log.info("Would have deleted: " + key + " from directory " + directory);
    }
}
