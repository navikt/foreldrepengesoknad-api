package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.health.AbstractPingableHealthIndicator;

@Component
public class HistorikkHealthIndicator extends AbstractPingableHealthIndicator {

    public HistorikkHealthIndicator(Historikk tjeneste) {
        super(tjeneste);
    }
}
