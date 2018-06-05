package no.nav.foreldrepenger.selvbetjening.felles.util;

import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UUIDCallIdGenerator {

    private final String callIdKey;

    @Inject
    public UUIDCallIdGenerator(@Value("${callid.key:Nav-CallId}") String callIdKey) {
        this.callIdKey = callIdKey;
    }

    public String getOrCreate() {
        return Optional.ofNullable(MDC.get(callIdKey)).orElse(doCreate());
    }

    public String create() {
        return doCreate();
    }

    private String doCreate() {
        return UUID.randomUUID().toString();
    }

    public String getCallIdKey() {
        return callIdKey;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [callIdKey=" + callIdKey + "]";
    }

}
