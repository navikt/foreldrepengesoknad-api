package no.nav.foreldrepenger.selvbetjening.health;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.Oppslag;

@Component
public class OppslagHealthIndicator extends AbstractEnvironmentAwareHealthIndicator {

    public OppslagHealthIndicator(Oppslag tjeneste) {
        super(tjeneste);
    }
}
