package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import java.util.Optional;

public interface Storage {

    void put(String directory, String key, String value);

    void putTmp(String directory, String key, String value);

    Optional<String> get(String directory, String key);

    Optional<String> getTmp(String directory, String key);

    void delete(String directory, String key);

    void deleteTmp(String directory, String key);
}
