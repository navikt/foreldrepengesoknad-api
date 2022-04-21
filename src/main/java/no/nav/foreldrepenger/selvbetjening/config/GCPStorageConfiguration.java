package no.nav.foreldrepenger.selvbetjening.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.threeten.bp.Duration;

import com.google.api.gax.retrying.RetrySettings;

import no.nav.foreldrepenger.boot.conditionals.ConditionalOnGCP;

@Configuration
@ConditionalOnGCP
public class GCPStorageConfiguration {

    @Bean
    RetrySettings retrySettings(@Value("${mellomlagring.timeout:3000}") int timeoutMs) {
        return RetrySettings.newBuilder()
            .setInitialRetryDelay(Duration.ofMillis(400))
            .setMaxRetryDelay(Duration.ofMillis(900))
            .setRetryDelayMultiplier(1.5)
            .setMaxAttempts(5)
            .setTotalTimeout(Duration.ofMillis(timeoutMs))
            .build();
    }
}
