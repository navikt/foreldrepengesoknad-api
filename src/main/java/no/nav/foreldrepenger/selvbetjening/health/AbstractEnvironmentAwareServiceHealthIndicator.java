package no.nav.foreldrepenger.selvbetjening.health;

import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.isDevOrPreprod;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import no.nav.foreldrepenger.selvbetjening.tjeneste.Pingable;

abstract class AbstractEnvironmentAwareHealthIndicator implements HealthIndicator, EnvironmentAware {

    private final Pingable pingable;
    private Environment env;

    public AbstractEnvironmentAwareHealthIndicator(Pingable pingable) {
        this.pingable = pingable;
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    protected void checkHealth() {
        pingable.ping();
    }

    @Override
    public Health health() {
        try {
            checkHealth();
            return up();
        } catch (Exception e) {
            return down(e);
        }
    }

    private Health up() {
        return isDevOrPreprod(env) ? Health.up().withDetail(pingable.name(), pingable.pingEndpoint()).build()
                : Health.up().build();
    }

    private Health down(Exception e) {
        return isDevOrPreprod(env)
                ? Health.down().withDetail(pingable.name(), pingable.pingEndpoint()).withException(e).build()
                : Health.down().build();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingable=" + pingable + "isDevOrPreprod "
                + isDevOrPreprod(env) + "]";
    }
}
