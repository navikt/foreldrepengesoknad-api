package no.nav.foreldrepenger.selvbetjening.historikk;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.health.AbstractPingableHealthIndicator;

@Component
public class HistorikkHealthIndicator extends AbstractPingableHealthIndicator {

    public HistorikkHealthIndicator(HistorikkConnection tjeneste) {
        super(tjeneste);
    }
}
