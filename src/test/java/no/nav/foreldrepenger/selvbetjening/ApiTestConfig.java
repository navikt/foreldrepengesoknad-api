package no.nav.foreldrepenger.selvbetjening;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import no.nav.foreldrepenger.selvbetjening.util.conditionals.ConditionalOnGCP;

@Configuration
public class ApiTestConfig {

    @Bean
    MeterRegistry meterRegistry() {
        return new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }

    @Bean
    @ConditionalOnGCP
    public String testjalla() {
        return "42";
    }
}
