package no.nav.foreldrepenger.selvbetjening.stub;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Storage;

public class StorageStub implements Storage {

    private static final Logger log = LoggerFactory.getLogger(StorageStub.class);

    @Override
    public void put(String directory, String key, String value) {
        log.info("Would have stored: " + key + " -> " + value + " in directory " + directory);
    }

    @Override
    public void putTmp(String directory, String key, String value) {
        log.info("Would have stored in tmp: " + key + " -> " + value + " in directory " + directory);
    }

    @Override
    public Optional<String> get(String directory, String key) {
        // Attachment JSON encrypted with key/fnr: 12345678910
        return Optional.of("cbA7LYNQFEbOkXmsv9ORXXXgNcx/Tf8iBsiPVeVofuaYRxesQz8tg/" +
                "lZRDtVWVGQHiRrFY/9LryCtkhdAWigaMuTPPw8ljzlmsV+pDOfaxsgbM/d0jPEqoo2y3bCQzLKBxOxTZZuhVlDCIb91Bx7BbiTd58hjkXytkoH0Jdt7m4=");
    }

    @Override
    public Optional<String> getTmp(String directory, String key) {
        return get(directory,key);
    }

    @Override
    public void delete(String directory, String key) {
        log.info("Would have deleted: " + key + " from directory " + directory);
    }

    @Override
    public void deleteTmp(String directory, String key) {
        log.info("Would have deleted from tmp: " + key + " from directory " + directory);

    }
}
