package no.nav.foreldrepenger.selvbetjening.config;

import java.net.URI;
import java.util.Optional;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Storage;

public class VoidStorage implements Storage {

    @Override
    public String ping() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public URI pingURI() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void put(String directory, String key, String value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putTmp(String directory, String key, String value) {
        // TODO Auto-generated method stub

    }

    @Override
    public Optional<String> get(String directory, String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<String> getTmp(String directory, String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(String directory, String key) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteTmp(String directory, String key) {
        // TODO Auto-generated method stub

    }

}
