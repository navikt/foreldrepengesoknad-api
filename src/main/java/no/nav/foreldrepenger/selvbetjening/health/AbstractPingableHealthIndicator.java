package no.nav.foreldrepenger.selvbetjening.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import no.nav.foreldrepenger.selvbetjening.tjeneste.Pingable;

public abstract class AbstractPingableHealthIndicator implements HealthIndicator {

    private final Pingable pingable;

    public AbstractPingableHealthIndicator(Pingable pingable) {
        this.pingable = pingable;
    }

    @Override
    public Health health() {
        try {
            pingable.ping();
            return up();
        } catch (Exception e) {
            return down(e);
        }
    }

    private Health up() {
        return Health.up()
                .withDetail(pingable.getClass().getSimpleName(), pingable.pingURI())
                .build();
    }

    private Health down(Exception e) {
        return Health.down()
                .withDetail(pingable.getClass().getSimpleName(), pingable.pingURI())
                .withException(e)
                .build();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingable=" + pingable + "]";
    }
}
