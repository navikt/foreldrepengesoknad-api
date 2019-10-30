package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static org.springframework.boot.cloud.CloudPlatform.KUBERNETES;

import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.health.AbstractPingableHealthIndicator;

@Component
@ConditionalOnCloudPlatform(KUBERNETES)
public class MellomlagringHealthIndicator extends AbstractPingableHealthIndicator {

    public MellomlagringHealthIndicator(Storage pingable) {
        super(pingable);
    }
}
