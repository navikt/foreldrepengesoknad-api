package no.nav.foreldrepenger.selvbetjening.felles.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.core.env.Environment;

public class EnvironmentAwareServiceHealthIndicator implements HealthIndicator {

    private final Pinger pinger;
    private final boolean isDevOrPreProd;

    public EnvironmentAwareServiceHealthIndicator(Environment env, Pinger pinger) {
        this.isDevOrPreProd = idDevOrPreprod(env);
        this.pinger = pinger;
    }

    private static boolean idDevOrPreprod(Environment env) {
        return env.acceptsProfiles("dev", "preprod");
    }

    @Override
    public Health health() {
        try {
            pinger.ping("Pinger");
            return isDevOrPreProd ? upWithDetails() : up();
        } catch (Exception e) {
            return isDevOrPreProd ? downWithDetails(e) : down();
        }
    }

    private static Health down() {
        return Health.down().build();
    }

    private Health downWithDetails(Exception e) {
        return Health.down().withDetail("uri", pinger.baseUri()).withException(e).build();
    }

    private static Health up() {
        return Health.up().build();
    }

    private Health upWithDetails() {
        return Health.up().withDetail("uri", pinger.baseUri()).build();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [url=" + pinger + "isDevOrPreProd "
                + isDevOrPreProd + "]";
    }
}
