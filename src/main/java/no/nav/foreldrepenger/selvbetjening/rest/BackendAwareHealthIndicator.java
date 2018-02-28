package no.nav.foreldrepenger.selvbetjening.rest;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;

import java.util.List;

import javax.inject.Inject;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.service.ping.Pinger;
import no.nav.foreldrepenger.selvbetjening.util.Pair;

@Component
public class BackendAwareHealthIndicator implements HealthIndicator {

    private final List<Pinger> pingServices;
    private final HealthAggregator aggregator;

    @Inject
    public BackendAwareHealthIndicator(HealthAggregator aggregator, Pinger... pingServices) {
        this.pingServices = asList(pingServices);
        this.aggregator = aggregator;
    }

    @Override
    public Health health() {
        return aggregator.aggregate(pingServices.stream()
                .map(BackendAwareHealthIndicator::ping)
                .collect(toMap(pair -> pair.getFirst(), pair -> pair.getSecond())));
    }

    private static Pair<String, Health> ping(Pinger pinger) {
        try {
            pinger.ping("hello");
            return Pair.of(pinger.baseUri().toString(), Health.up().build());
        } catch (Exception e) {
            return Pair.of(pinger.baseUri().toString(),
                    Health.down().withException(e).build());
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingServices=" + pingServices + ", aggregator=" + aggregator + "]";
    }

}
