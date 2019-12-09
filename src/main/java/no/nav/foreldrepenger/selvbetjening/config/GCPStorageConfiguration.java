package no.nav.foreldrepenger.selvbetjening.config;

import static no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Bøtte.SØKNAD;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Bøtte.TMP;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.threeten.bp.Duration;

import com.google.api.gax.retrying.RetrySettings;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Bøtte;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.GCPMellomlagring;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Mellomlagring;
import no.nav.foreldrepenger.selvbetjening.util.conditionals.ConditionalOnGCP;

@Configuration
@ConditionalOnGCP
public class GCPStorageConfiguration {

    @Bean
    RetrySettings retrySettings(@Value("${mellomlagring.timeout:3000}") int timeoutMs) {
        return RetrySettings.newBuilder()
                .setTotalTimeout(Duration.ofMillis(timeoutMs))
                .build();
    }

    @Bean
    public Mellomlagring gcpMellomlagring(
            @Qualifier(SØKNAD) Bøtte søknad,
            @Qualifier(TMP) Bøtte mellomlagring, RetrySettings retrySettings) {
        return new GCPMellomlagring(søknad, mellomlagring, retrySettings);
    }
}
