package no.nav.foreldrepenger.selvbetjening.health;

import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.isDevOrLocal;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import no.nav.foreldrepenger.selvbetjening.tjeneste.Pingable;

public abstract class AbstractEnvironmentAwareHealthIndicator implements HealthIndicator, EnvironmentAware {

    private final Pingable pingable;
    private Environment env;

    public AbstractEnvironmentAwareHealthIndicator(Pingable pingable) {
        this.pingable = pingable;
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
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
        return isDevOrLocal(env)
                ? Health.up()
                        .withDetail(pingable.getClass().getSimpleName(), pingable.pingURI())
                        .build()
                : Health.up().build();
    }

    private Health down(Exception e) {
        return isDevOrLocal(env)
                ? Health.down()
                        .withDetail(pingable.getClass().getSimpleName(), pingable.pingURI())
                        .withException(e)
                        .build()
                : Health.down().build();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingable=" + pingable + "isDevOrLocal "
                + isDevOrLocal(env) + "]";
    }
}
