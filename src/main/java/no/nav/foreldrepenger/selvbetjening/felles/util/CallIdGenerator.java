package no.nav.foreldrepenger.selvbetjening.felles.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class CallIdGenerator {

    public String create() {
        return UUID.randomUUID().toString();
    }
}
