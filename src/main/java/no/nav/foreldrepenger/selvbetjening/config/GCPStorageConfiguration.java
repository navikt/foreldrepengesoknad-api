package no.nav.foreldrepenger.selvbetjening.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.threeten.bp.Duration;

import com.google.api.gax.retrying.RetrySettings;
import com.google.cloud.ServiceOptions;

import no.nav.foreldrepenger.selvbetjening.mellomlagring.Bøtte;

@Configuration
public class GCPStorageConfiguration {

    @Bean
    RetrySettings retrySettings(@Value("${mellomlagring.timeout:5000}") int timeoutMs) {
        return ServiceOptions.getDefaultRetrySettings().toBuilder()
            .setInitialRetryDelay(Duration.ofMillis(400))
            .setMaxRetryDelay(Duration.ofMillis(900))
            .setRetryDelayMultiplier(1.5)
            .setMaxAttempts(5)
            .setTotalTimeout(Duration.ofMillis(timeoutMs))
            .build();
    }

    @Bean
    public Bøtte tmpBøtte(
        @Value("${mellomlagring.tmp.navn:mellomlagring}") String navn,
        @Value("${mellomlagring.tmp.levetid:1d}") java.time.Duration levetid,
        @Value("${mellomlagring.tmp.enabled:true}") boolean enabled) {
        return new Bøtte(navn, levetid, enabled);
    }
}
