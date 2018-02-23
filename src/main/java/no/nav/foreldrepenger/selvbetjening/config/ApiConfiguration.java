package no.nav.foreldrepenger.selvbetjening.config;

import java.net.URI;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import no.nav.foreldrepenger.selvbetjening.rest.ApiKeyInsertingClientInterceptor;

@Configuration
public class ApiConfiguration {

    // @Value("${FPSOKNAD_MOTTAK_API_URL}")
    private String mottakServiceUrl;

    @Bean
    public RestTemplate restTemplate(@Value("${FPSOKNAD_MOTTAK_API_URL}") URI uri,
            ClientHttpRequestInterceptor clientHttpRequestInterceptor) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(clientHttpRequestInterceptor));
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestInterceptor apiKeyInsertingClientInterceptor(
            @Value("${FORELDREPENGESOKNAD_API_FPSOKNAD_MOTTAK_API_APIKEY_PASSWORD}") String apiKey) {
        return new ApiKeyInsertingClientInterceptor(apiKey);

    }
}
