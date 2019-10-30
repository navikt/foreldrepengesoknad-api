package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.health.AbstractPingableHealthIndicator;
import no.nav.foreldrepenger.selvbetjening.util.ConditionalOnK8s;

@Component
@ConditionalOnK8s
public class MellomlagringHealthIndicator extends AbstractPingableHealthIndicator {

    public MellomlagringHealthIndicator(Storage pingable) {
        super(pingable);
    }
}
