package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.health.AbstractEnvironmentAwareHealthIndicator;

@Component
public class OppslagHealthIndicator extends AbstractEnvironmentAwareHealthIndicator {

    public OppslagHealthIndicator(Oppslag tjeneste) {
        super(tjeneste);
    }
}
