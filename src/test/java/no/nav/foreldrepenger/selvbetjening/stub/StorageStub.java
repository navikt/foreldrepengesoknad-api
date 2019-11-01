package no.nav.foreldrepenger.selvbetjening.stub;

import java.net.URI;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.MellomlagringTjeneste;

public class StorageStub implements MellomlagringTjeneste {

    private static final Logger log = LoggerFactory.getLogger(StorageStub.class);

    @Override
    public void lagre(String directory, String key, String value) {
        log.info("Would have stored: " + key + " -> " + value + " in directory " + directory);
    }

    @Override
    public void lagreTmp(String directory, String key, String value) {
        log.info("Would have stored in tmp: " + key + " -> " + value + " in directory " + directory);
    }

    @Override
    public Optional<String> les(String directory, String key) {
        // Attachment JSON encrypted with key/fnr: 12345678910
        return Optional.of("cbA7LYNQFEbOkXmsv9ORXXXgNcx/Tf8iBsiPVeVofuaYRxesQz8tg/" +
                "lZRDtVWVGQHiRrFY/9LryCtkhdAWigaMuTPPw8ljzlmsV+pDOfaxsgbM/d0jPEqoo2y3bCQzLKBxOxTZZuhVlDCIb91Bx7BbiTd58hjkXytkoH0Jdt7m4=");
    }

    @Override
    public Optional<String> lesTmp(String directory, String key) {
        return les(directory, key);
    }

    @Override
    public void slett(String directory, String key) {
        log.info("Would have deleted: " + key + " from directory " + directory);
    }

    @Override
    public void slettTmp(String directory, String key) {
        log.info("Would have deleted from tmp: " + key + " from directory " + directory);

    }

    @Override
    public String ping() {
        return "OK";
    }

    @Override
    public URI pingURI() {
        return URI.create("http://localhost/stub");
    }
}
