package no.nav.foreldrepenger.selvbetjening;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import no.nav.foreldrepenger.selvbetjening.util.Cluster;
import no.nav.foreldrepenger.selvbetjening.util.ConditionalOnClusters;

@Configuration
public class ApiTestConfig {

    @Bean
    MeterRegistry meterRegistry() {
        return new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }

    @Bean
    @ConditionalOnClusters(clusters = { Cluster.DEV_GCP, Cluster.PROD_GCP })
    public String testjalla() {
        return "42";
    }
}
