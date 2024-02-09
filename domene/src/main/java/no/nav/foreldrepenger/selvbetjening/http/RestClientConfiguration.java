package no.nav.foreldrepenger.selvbetjening.http;

import static no.nav.foreldrepenger.common.util.Constants.FNR;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.retry.RetryContext.NAME;
import static org.springframework.util.ReflectionUtils.findField;
import static org.springframework.util.ReflectionUtils.getField;
import static org.springframework.util.ReflectionUtils.makeAccessible;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.web.client.RestOperations;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenService;
import no.nav.security.token.support.client.spring.ClientConfigurationProperties;
import no.nav.security.token.support.client.spring.oauth2.ClientConfigurationPropertiesMatcher;
import no.nav.security.token.support.client.spring.oauth2.OAuth2ClientRequestInterceptor;

@Configuration
public class RestClientConfiguration implements WebMvcConfigurer {

    public static final String LONG_TIMEOUT = "longTimeoutResttemplate";
    private static final Logger LOG = LoggerFactory.getLogger(RestClientConfiguration.class);

    private final String[] allowedOrigins;

    public RestClientConfiguration(@Value("${allowed.origins}") String[] allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @Bean
    @Qualifier(LONG_TIMEOUT)
    public RestOperations longTimeoutRestTemplate(RestTemplateBuilder b, ClientHttpRequestInterceptor... interceptors) {
        return b.interceptors(interceptors)
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(40))
            .build();
    }

    @Bean
    @Primary
    public RestOperations regularTimeoutRestTemplate(RestTemplateBuilder b, ClientHttpRequestInterceptor... interceptors) {
        return b.interceptors(interceptors)
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(10))
            .build();
    }

    @Bean
    public RestTemplateBuilder tokendingsRestTemplateBuilder(RestTemplateBuilderConfigurer configurer) {
        // RestTemplateBuilder tas inn av DefaultOAuth2HttpClient. Setter default timeout verdier for denne.
        return configurer.configure(new RestTemplateBuilder())
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(10));
    }

    @Bean
    @Order(HIGHEST_PRECEDENCE)
    public OAuth2ClientRequestInterceptor tokenExchangeClientRequestInterceptor(ClientConfigurationProperties properties,
                                                                                OAuth2AccessTokenService service,
                                                                                ClientConfigurationPropertiesMatcher matcher) {
        return new OAuth2ClientRequestInterceptor(properties, service, matcher);
    }

    @Bean
    public ClientConfigurationPropertiesMatcher tokenxClientConfigMatcher() {
        return (properties, uri) -> {
            LOG.trace("Oppslag token X konfig for {}", uri.getHost());
            var cfg = properties.getRegistration().get(uri.getHost().split("\\.")[0]);
            if (cfg != null) {
                LOG.trace("Oppslag token X konfig for {} OK", uri.getHost());
            } else {
                LOG.trace("Oppslag token X konfig for {} fant ingenting", uri.getHost());
            }
            return Optional.ofNullable(cfg);
        };
    }

    @Bean
    public List<RetryListener> retryListeners() {
        return List.of(new RetryListener() {

            @Override
            public <T, E extends Throwable> void onError(RetryContext ctx, RetryCallback<T, E> callback, Throwable t) {
                LOG.info("Metode {} kastet exception {} for {}. gang", ctx.getAttribute(NAME), t, ctx.getRetryCount());
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext ctx, RetryCallback<T, E> callback, Throwable t) {
                if (t != null) {
                    LOG.warn("Metode {} avslutter ikke-vellykket retry etter {}. forsøk ({})",
                        ctx.getAttribute(NAME), ctx.getRetryCount(), t.getMessage(), t);
                } else {
                    if (ctx.getRetryCount() > 0) {
                        LOG.info("Metode {} avslutter vellykket retry etter {}. forsøk",
                            ctx.getAttribute(NAME), ctx.getRetryCount());
                    }
                }
            }

            @Override
            public <T, E extends Throwable> boolean open(RetryContext ctx, RetryCallback<T, E> callback) {
                var labelField = findField(callback.getClass(), "val$label");
                makeAccessible(labelField);
                var m = String.class.cast(getField(labelField, callback));
                if (ctx.getRetryCount() > 0) {
                    LOG.info("Metode {} gjør retry for {}. gang", m, ctx.getRetryCount());
                }
                return true;
            }
        });
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods(POST.name(), GET.name(), OPTIONS.name(), DELETE.name())
                .allowCredentials(true)
                .exposedHeaders(LOCATION)
                .allowedHeaders(FNR, CONTENT_TYPE)
                .allowedOriginPatterns(allowedOrigins);
    }
}
