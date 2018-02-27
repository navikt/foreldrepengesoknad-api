package no.nav.foreldrepenger.selvbetjening.rest;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.service.PingService;

@Component
public class BackendAwareHealthIndicator implements HealthIndicator {

    private final PingService pingService;

    public BackendAwareHealthIndicator(PingService pingService) {
        this.pingService = pingService;
    }

    @Override
    public Health health() {
        try {
            pingService.ping("hello");
            return Health.up().withDetail("remote", pingService.remote()).build();
        } catch (Exception e) {
            return Health.down().withDetail("message", e.getMessage()).withException(e).build();
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingService=" + pingService + "]";
    }
}
