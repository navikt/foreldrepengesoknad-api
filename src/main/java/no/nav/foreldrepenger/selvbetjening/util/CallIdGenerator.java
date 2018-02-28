package no.nav.foreldrepenger.selvbetjening.util;

public interface CallIdGenerator {

    Pair<String, String> generateCallId();

    String generateCallId(String key);

    String getDefaultKey();

}
