package no.nav.foreldrepenger.selvbetjening.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class CallIdGenerator {

    public String create() {
        return UUID.randomUUID().toString();
    }
}
