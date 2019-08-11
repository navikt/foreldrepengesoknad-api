package no.nav.foreldrepenger.selvbetjening.config;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.X_NAV_API_KEY;

import java.net.URI;
import java.util.Arrays;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestOperations;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.common.collect.ImmutableMap;

import no.nav.foreldrepenger.selvbetjening.filters.CorsInterceptor;
import no.nav.foreldrepenger.selvbetjening.interceptors.client.ApiKeyInjectingClientInterceptor;
import no.nav.foreldrepenger.selvbetjening.tjeneste.historikk.HistorikkConfig;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.InnsendingConfig;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.OppslagConfig;

@Configuration
public class ApiConfiguration implements WebMvcConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(ApiConfiguration.class);

    @Inject
    CorsInterceptor corsInterceptor;

    @Bean
    public RestOperations restTemplate(ClientHttpRequestInterceptor... interceptors) {
        LOG.info("Registrerer intereptorer {}", Arrays.toString(interceptors));
        return new RestTemplateBuilder()
                .interceptors(interceptors)
                .build();
    }

    @Bean
    public ClientHttpRequestInterceptor apiKeyInjectingClientInterceptor(OppslagConfig oppslag,
            InnsendingConfig innsending, HistorikkConfig historikk) {
        return new ApiKeyInjectingClientInterceptor(X_NAV_API_KEY,
                ImmutableMap.<URI, String>builder()
                        .put(innsending.getURI(), innsending.getApiKey())
                        .put(oppslag.getURI(), oppslag.getApiKey())
                        .put(historikk.getURI(), historikk.getApiKey())
                        .build());

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(corsInterceptor);
    }
}
