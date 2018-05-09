package no.nav.foreldrepenger.selvbetjening.felles.util;

import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UUIDCallIdGenerator implements CallIdGenerator {

    private final String key;

    @Inject
    public UUIDCallIdGenerator(@Value("${callid.key:X-Nav-CallId}") String key) {
        this.key = key;
    }

    @Override
    public String getOrCreate() {
        return Optional.ofNullable(MDC.get(key)).orElse(doCreate());
    }

    @Override
    public String create() {
        return doCreate();
    }

    private String doCreate() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [key=" + key + "]";
    }

}
