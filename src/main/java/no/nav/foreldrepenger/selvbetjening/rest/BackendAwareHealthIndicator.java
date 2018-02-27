package no.nav.foreldrepenger.selvbetjening.rest;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.service.Pair;
import no.nav.foreldrepenger.selvbetjening.service.Pinger;

@Component
public class BackendAwareHealthIndicator implements HealthIndicator {

    private final List<Pinger> pingServices;
    private final HealthAggregator aggregator;

    @Inject
    public BackendAwareHealthIndicator(HealthAggregator aggregator, Pinger... pingServices) {
        this.pingServices = Arrays.asList(pingServices);
        this.aggregator = aggregator;
    }

    @Override
    public Health health() {
        return aggregator.aggregate(pingServices.stream()
                .map(this::ping)
                .collect(toMap(pair -> pair.getFirst(), pair -> pair.getSecond())));
    }

    private Pair<String, Health> ping(Pinger pinger) {
        try {
            pinger.ping("hello");
            return Pair.of(pinger.baseUri().toString(), Health.up().withDetail("remote", pinger.baseUri()).build());
        } catch (Exception e) {
            return Pair.of(pinger.baseUri().toString(),
                    Health.down().withDetail("message", e.getMessage()).withException(e).build());
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingServices=" + pingServices + ", aggregator=" + aggregator + "]";
    }

}
