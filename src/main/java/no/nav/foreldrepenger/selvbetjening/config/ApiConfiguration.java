package no.nav.foreldrepenger.selvbetjening.config;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.common.collect.ImmutableMap;

import no.nav.foreldrepenger.selvbetjening.filters.CorsInterceptor;
import no.nav.foreldrepenger.selvbetjening.interceptors.client.ApiKeyInjectingClientInterceptor;
import no.nav.foreldrepenger.selvbetjening.tjeneste.historikk.HistorikkConfig;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.InnsendingConfig;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.StorageCrypto;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.OppslagConfig;

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
    public ClientHttpRequestInterceptor apiKeyInjectingClientInterceptor(OppslagConfig oppslag, InnsendingConfig mottak,
            HistorikkConfig historikk) {
        var builder = ImmutableMap.<URI, String>builder();
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

    @Bean
    public List<RetryListener> retryListeners() {
        Logger log = LoggerFactory.getLogger(getClass());

        return Collections.singletonList(new RetryListener() {

            @Override
            public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
                Field labelField = ReflectionUtils.findField(callback.getClass(), "val$label");
                ReflectionUtils.makeAccessible(labelField);
                String label = (String) ReflectionUtils.getField(labelField, callback);
                log.trace("Starting retryable method {}", label);
                return true;
            }

            @Override
            public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
                    Throwable throwable) {
                log.warn("Retryable method {} threw {}th exception {}",
                        context.getAttribute("context.name"), context.getRetryCount(), throwable.toString());
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback,
                    Throwable throwable) {
                log.trace("Finished retryable method {} {}", context.getRetryCount(),
                        context.getAttribute("context.name"));
            }
        });
    }

    @Bean
    public StorageCrypto storageCrypto(@Value("${storage.passphrase}") String passPhrase) {
        return new StorageCrypto(passPhrase);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(corsInterceptor);
    }
}
