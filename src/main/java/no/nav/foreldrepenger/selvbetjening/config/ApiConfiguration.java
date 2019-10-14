package no.nav.foreldrepenger.selvbetjening.config;

import java.net.URI;
import java.util.Arrays;

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
import com.google.common.collect.ImmutableMap.Builder;

import no.nav.foreldrepenger.selvbetjening.filters.CorsInterceptor;
import no.nav.foreldrepenger.selvbetjening.interceptors.client.ApiKeyInjectingClientInterceptor;
import no.nav.foreldrepenger.selvbetjening.tjeneste.historikk.HistorikkConfig;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.InnsendingConfig;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.OppslagConfig;
import no.nav.foreldrepenger.selvbetjening.util.ConditionalOnSBS;

@Configuration
public class ApiConfiguration implements WebMvcConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(ApiConfiguration.class);
    private final CorsInterceptor corsInterceptor;

    public ApiConfiguration(CorsInterceptor corsInterceptor) {
        this.corsInterceptor = corsInterceptor;
    }

    @Bean
    public RestOperations restTemplate(ClientHttpRequestInterceptor... interceptors) {
        LOG.info("Registrerer interceptorer {}", Arrays.toString(interceptors));
        return new RestTemplateBuilder()
                .interceptors(interceptors)
                .build();
    }

    @Bean
    @ConditionalOnSBS
    public ClientHttpRequestInterceptor apiKeyInjectingClientInterceptor(OppslagConfig oppslag, InnsendingConfig mottak,
            HistorikkConfig historikk) {
        Builder<URI, String> builder = ImmutableMap.<URI, String>builder();
        builder.put(oppslag.getUri(), oppslag.getKey());
        builder.put(mottak.getUri(), mottak.getKey());
        builder.put(historikk.getUri(), historikk.getKey());
        return new ApiKeyInjectingClientInterceptor(builder.build());

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
