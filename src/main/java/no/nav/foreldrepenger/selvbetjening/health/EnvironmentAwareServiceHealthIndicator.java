package no.nav.foreldrepenger.selvbetjening.health;

import java.util.Arrays;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.core.env.Environment;

public class EnvironmentAwareServiceHealthIndicator implements HealthIndicator {

    private final Pinger pinger;
    private final Environment env;

    public EnvironmentAwareServiceHealthIndicator(Environment env, Pinger pinger) {
        this.env = env;
        this.pinger = pinger;
    }

    @Override
    public Health health() {
        try {
            pinger.ping("Pinger");
            return isPreprodOrDev() ? upWithDetails() : up();
        } catch (Exception e) {
            return isPreprodOrDev() ? downWithDetails(e) : down();
        }
    }

    private static Health down() {
        return Health.down().build();
    }

    private Health downWithDetails(Exception e) {
        return Health.down().withDetail("uri", pinger.baseUri()).withException(e).build();
    }

    private boolean isPreprodOrDev() {
        return env.acceptsProfiles("dev", "preprod");
    }

    private static Health up() {
        return Health.up().build();
    }

    private Health upWithDetails() {
        return Health.up().withDetail("uri", pinger.baseUri()).build();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [url=" + pinger + "activeProfiles "
                + Arrays.toString(env.getActiveProfiles()) + "]";
    }
}
