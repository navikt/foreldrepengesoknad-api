package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.health.AbstractPingableHealthIndicator;

@Component
public class InnsynHealthIndicator extends AbstractPingableHealthIndicator {

    public InnsynHealthIndicator(InnsynConnection tjeneste) {
        super(tjeneste);
    }
}
