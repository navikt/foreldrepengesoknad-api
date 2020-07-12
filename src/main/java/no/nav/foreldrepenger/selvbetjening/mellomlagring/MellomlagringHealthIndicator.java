package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.health.AbstractPingableHealthIndicator;

@Component
public class MellomlagringHealthIndicator extends AbstractPingableHealthIndicator {

    public MellomlagringHealthIndicator(Mellomlagring pingable) {
        super(pingable);
    }
}
