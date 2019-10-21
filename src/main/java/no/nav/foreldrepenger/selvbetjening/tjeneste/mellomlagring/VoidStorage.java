package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import java.net.URI;
import java.util.Optional;

public class VoidStorage implements Storage {

    @Override
    public String ping() {
        return "OK";
    }

    @Override
    public URI pingURI() {
        return URI.create("http://localhost");
    }

    @Override
    public void put(String directory, String key, String value) {

    }

    @Override
    public void putTmp(String directory, String key, String value) {

    }

    @Override
    public Optional<String> get(String directory, String key) {
        return Optional.empty();
    }

    @Override
    public Optional<String> getTmp(String directory, String key) {
        return Optional.empty();
    }

    @Override
    public void delete(String directory, String key) {

    }

    @Override
    public void deleteTmp(String directory, String key) {

    }

}
