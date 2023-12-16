package no.nav.foreldrepenger.selvbetjening.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.threeten.bp.Duration;

import com.google.cloud.ServiceOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import no.nav.foreldrepenger.selvbetjening.mellomlagring.Bøtte;

@Configuration
public class GCPStorageConfiguration {

    @Bean
    Storage gcpStorage(@Value("${mellomlagring.timeout:5000}") int timeoutMs) {
        var retrySettings = ServiceOptions.getDefaultRetrySettings().toBuilder()
            .setInitialRetryDelay(Duration.ofMillis(400))
            .setMaxRetryDelay(Duration.ofMillis(900))
            .setRetryDelayMultiplier(1.5)
            .setMaxAttempts(5)
            .setTotalTimeout(Duration.ofMillis(timeoutMs))
            .build();

        return StorageOptions
            .newBuilder()
            .setRetrySettings(retrySettings)
            .build()
            .getService();
    }

    @Bean
    public Bøtte tmpBøtte(
        @Value("${mellomlagring.tmp.navn:mellomlagring}") String navn,
        @Value("${mellomlagring.tmp.levetid:1d}") java.time.Duration levetid) {
        return new Bøtte(navn, levetid);
    }
}
