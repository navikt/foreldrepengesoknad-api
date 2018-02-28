package no.nav.foreldrepenger.selvbetjening.config;

import static java.util.Collections.singletonList;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.OrderedHealthAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.ImmutableMap;

import no.nav.foreldrepenger.selvbetjening.rest.util.ApiKeyInjectingClientInterceptor;

@Configuration
public class ApiConfiguration {

    @Value("${apikeys.key:x-nav-apiKey}")
    private String key;

    @Value("${FPSOKNAD_MOTTAK_API_URL}")
    private URI mottakServiceUri;

    @Value("${FPSOKNAD_OPPSLAG_API_URL}")
    private URI oppslagServiceUri;

    @Value("${FORELDREPENGESOKNAD_API_FPSOKNAD_MOTTAK_API_APIKEY_PASSWORD}")
    String mottakApiKey;

    @Value("${FORELDREPENGESOKNAD_API_FPSOKNAD_OPPSLAG_API_APIKEY_PASSWORD}")
    String oppslagApiKey;

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestInterceptor interceptor) {
        RestTemplate template = new RestTemplate();
        template.setInterceptors(singletonList(interceptor));
        return template;
    }

    @Bean
    public ClientHttpRequestInterceptor apiKeyInjectingClientInterceptor() {
        return new ApiKeyInjectingClientInterceptor(key,
                ImmutableMap.<URI, String>builder()
                        .put(mottakServiceUri, mottakApiKey)
                        .put(oppslagServiceUri, oppslagApiKey)
                        .build());

    }

    @Bean
    public HealthAggregator healthAggregator() {
        return new OrderedHealthAggregator();
    }
}
