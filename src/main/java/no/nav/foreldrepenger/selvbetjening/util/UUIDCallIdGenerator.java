package no.nav.foreldrepenger.selvbetjening.util;

import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UUIDCallIdGenerator implements CallIdGenerator {

    private final String defaultCallIdkey;

    @Inject
    public UUIDCallIdGenerator(@Value("${callid.key:X-Nav-CallId}") String defaultCallIdkey) {
        this.defaultCallIdkey = defaultCallIdkey;
    }

    @Override
    public String generateCallId(String key) {
        return getOrCreate(key);
    }

    @Override
    public Pair<String, String> generateCallId() {
        return Pair.of(defaultCallIdkey, getOrCreate(defaultCallIdkey));
    }

    @Override
    public String getDefaultKey() {
        return defaultCallIdkey;
    }

    private static String getOrCreate(String key) {
        String callId = Optional.ofNullable(MDC.get(key)).orElse(UUID.randomUUID().toString());
        MDC.put(key, callId);
        return callId;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [defaultCallIdkey=" + defaultCallIdkey + "]";
    }

}
