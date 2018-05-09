package no.nav.foreldrepenger.selvbetjening.felles.config;

import static java.util.Arrays.asList;

import java.net.URI;

import javax.inject.Inject;

import no.nav.foreldrepenger.selvbetjening.felles.filters.CorsInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.common.collect.ImmutableMap;

import no.nav.foreldrepenger.selvbetjening.felles.health.EnvironmentAwareServiceHealthIndicator;
import no.nav.foreldrepenger.selvbetjening.felles.health.MottakPingService;
import no.nav.foreldrepenger.selvbetjening.felles.health.OppslagPingService;
import no.nav.foreldrepenger.selvbetjening.felles.filters.ApiKeyInjectingClientInterceptor;

@Configuration
public class ApiConfiguration implements WebMvcConfigurer {

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

    @Inject
    CorsInterceptor corsInterceptor;

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestInterceptor... interceptors) {
        RestTemplate template = new RestTemplate();
        template.setInterceptors(asList(interceptors));
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
    HealthIndicator oppslag(Environment env, OppslagPingService service) {
        return new EnvironmentAwareServiceHealthIndicator(env, service);
    }

    @Bean
    HealthIndicator mottak(Environment env, MottakPingService service) {
        return new EnvironmentAwareServiceHealthIndicator(env, service);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(corsInterceptor);
    }
}
