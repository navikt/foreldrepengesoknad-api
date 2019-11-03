package no.nav.foreldrepenger.selvbetjening.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import no.nav.foreldrepenger.selvbetjening.tjeneste.PingEndpointAware;

public abstract class AbstractPingableHealthIndicator implements HealthIndicator {

    private final PingEndpointAware pingable;

    public AbstractPingableHealthIndicator(PingEndpointAware pingable) {
        this.pingable = pingable;
    }

    @Override
    public Health health() {
        try {
            pingable.ping();
            return Health.up()
                    .withDetail(pingable.getClass().getSimpleName(), pingable.pingURI())
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail(pingable.getClass().getSimpleName(), pingable.pingURI())
                    .withException(e)
                    .build();
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingable=" + pingable + "]";
    }
}
