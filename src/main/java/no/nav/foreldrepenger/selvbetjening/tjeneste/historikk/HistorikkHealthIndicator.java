package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.health.AbstractEnvironmentAwareHealthIndicator;

@Component
public class HistorikkHealthIndicator extends AbstractEnvironmentAwareHealthIndicator {

    public HistorikkHealthIndicator(Historikk tjeneste) {
        super(tjeneste);
    }
}
