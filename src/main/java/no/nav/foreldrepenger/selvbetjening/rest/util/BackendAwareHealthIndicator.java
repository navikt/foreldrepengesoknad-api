package no.nav.foreldrepenger.selvbetjening.rest.util;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;

import java.util.List;

import javax.inject.Inject;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.consumer.ping.Pinger;
import no.nav.foreldrepenger.selvbetjening.util.Pair;

@Component
public class BackendAwareHealthIndicator implements HealthIndicator {

    private final Environment env;
    private final List<Pinger> pingServices;
    private final HealthAggregator aggregator;

    @Inject
    public BackendAwareHealthIndicator(Environment env, HealthAggregator aggregator, Pinger... pingServices) {
        this.env = env;
        this.pingServices = asList(pingServices);
        this.aggregator = aggregator;
    }

    @Override
    public Health health() {
        return aggregator.aggregate(pingServices.stream()
                .map(this::ping)
                .collect(toMap(Pair::getFirst, Pair::getSecond)));
    }

    private Pair<String, Health> ping(Pinger pinger) {
        try {
            pinger.ping("hello");
            return isPreprodOrDev() ? upWithDetails(pinger) : up(pinger);
        } catch (Exception e) {
            return isPreprodOrDev() ? downWithDetails(pinger, e) : down(pinger);

        }
    }

    private static Pair<String, Health> down(Pinger pinger) {
        return Pair.of(pinger.baseUri().toString(), Health.down().build());
    }

    private Pair<String, Health> downWithDetails(Pinger pinger, Exception e) {
        return Pair.of(pinger.baseUri().toString(),
                Health.down().withDetail("uri", pinger.baseUri().toString()).withException(e).build());
    }

    private boolean isPreprodOrDev() {
        return env.acceptsProfiles("dev", "preprod");
    }

    private static Pair<String, Health> up(Pinger pinger) {
        return Pair.of(pinger.baseUri().toString(), Health.up().build());
    }

    private Pair<String, Health> upWithDetails(Pinger pinger) {
        return Pair.of(pinger.baseUri().toString(), Health.up().withDetail("uri", pinger.baseUri().toString()).build());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingServices=" + pingServices + ", aggregator=" + aggregator + "]";
    }

}
