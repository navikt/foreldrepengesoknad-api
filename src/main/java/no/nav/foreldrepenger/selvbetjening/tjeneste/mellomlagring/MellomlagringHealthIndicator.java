package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.health.AbstractPingableHealthIndicator;

@Component
public class MellomlagringHealthIndicator extends AbstractPingableHealthIndicator {

    public MellomlagringHealthIndicator(MellomlagringTjeneste pingable) {
        super(pingable);
    }
}
