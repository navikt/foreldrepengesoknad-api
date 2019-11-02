package no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.health.AbstractPingableHealthIndicator;

@Component
public class VirusScanHealthIndicator extends AbstractPingableHealthIndicator {

    public VirusScanHealthIndicator(VirusScanConnection tjeneste) {
        super(tjeneste);
    }
}
