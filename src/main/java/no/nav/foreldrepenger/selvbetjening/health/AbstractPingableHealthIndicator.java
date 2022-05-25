package no.nav.foreldrepenger.selvbetjening.health;

import java.util.Map;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import no.nav.foreldrepenger.selvbetjening.http.PingEndpointAware;

public abstract class AbstractPingableHealthIndicator implements HealthIndicator {

    private final PingEndpointAware pingable;

    protected AbstractPingableHealthIndicator(PingEndpointAware pingable) {
        this.pingable = pingable;
    }

    @Override
    public Health health() {
        try {
            if (pingable.isEnabled()) {
                pingable.ping();
                return up();
            }
            return up();
        } catch (Exception e) {
            return Health.down()
                    .withDetail(pingable.name(), pingable.pingURI())
                    .withException(e)
                    .build();
        }
    }

    private Health up() {
        return Health.up()
                .withDetails(Map.of(pingable.name(), pingable.pingURI(), "enabled", pingable.isEnabled()))
                .build();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingable=" + pingable + "]";
    }
}
