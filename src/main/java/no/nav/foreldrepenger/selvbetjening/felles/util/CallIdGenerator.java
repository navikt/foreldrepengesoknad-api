package no.nav.foreldrepenger.selvbetjening.felles.util;

public interface CallIdGenerator {

    String getOrCreate();

    String create();

    String getKey();

}
