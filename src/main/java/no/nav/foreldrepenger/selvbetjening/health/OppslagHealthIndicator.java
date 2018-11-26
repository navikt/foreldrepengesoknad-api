package no.nav.foreldrepenger.selvbetjening.health;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.OppslagConnection;

@Component
public class OppslagHealthIndicator extends AbstractEnvironmentAwareHealthIndicator {

    public OppslagHealthIndicator(OppslagConnection connection) {
        super(connection);
    }
}
