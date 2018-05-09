package no.nav.foreldrepenger.selvbetjening.felles.storage;

public interface Storage {

    void put(String key, String value);

    String get(String key);

    void delete(String key);
}
