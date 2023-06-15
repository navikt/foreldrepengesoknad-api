package no.nav.foreldrepenger.selvbetjening.innsyn;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.health.AbstractPingableHealthIndicator;

@Component
public class OversiktHealthIndicator extends AbstractPingableHealthIndicator {

    public OversiktHealthIndicator(OversiktConnection tjeneste) {
        super(tjeneste);
    }
}
