package no.nav.foreldrepenger.selvbetjening.oppslag;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.health.AbstractPingableHealthIndicator;

@Component
public class OppslagHealthIndicator extends AbstractPingableHealthIndicator {

    public OppslagHealthIndicator(OppslagConnection tjeneste) {
        super(tjeneste);
    }
}
