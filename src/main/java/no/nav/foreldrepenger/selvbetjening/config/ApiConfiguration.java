package no.nav.foreldrepenger.selvbetjening.config;

import static java.util.Collections.singletonList;
import static java.util.function.Predicate.not;
import static no.nav.foreldrepenger.common.util.Constants.FNR;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.retry.RetryContext.NAME;

import java.time.Duration;
import java.util.Arrays;
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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.common.base.Splitter;

import no.nav.foreldrepenger.selvbetjening.http.interceptors.TokenXConfigFinder;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.Bøtte;
import no.nav.security.token.support.spring.validation.interceptor.BearerTokenClientHttpRequestInterceptor;

@Configuration
public class ApiConfiguration implements WebMvcConfigurer {
    private static final Logger LOG = LoggerFactory.getLogger(ApiConfiguration.class);

    private final String[] allowedOrigins;

    public ApiConfiguration(@Value("${allowed.origins}") String[] allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @Bean
    public RestOperations tokenXTemplate(RestTemplateBuilder b, ClientHttpRequestInterceptor... interceptors) {
        var filtered = filtrerBortBearerTokenInterceptor(interceptors);
        LOG.info("Registrerer interceptorer med TokenX interceptor {}", filtered);
        return b
            .interceptors(filtered)
            .build();
    }

    private static List<ClientHttpRequestInterceptor> filtrerBortBearerTokenInterceptor(ClientHttpRequestInterceptor... interceptors) {
        return Arrays.stream(interceptors)
            .filter(not(i -> i.getClass().equals(BearerTokenClientHttpRequestInterceptor.class)))
            .toList();
    }

    @Bean
    public TokenXConfigFinder configFinder() {
        return (cfgs, req) -> {
            LOG.trace("Oppslag token X konfig for {}", req.getHost());
            var cfg = cfgs.getRegistration().get(Splitter.on(".").splitToList(req.getHost()).get(0));
            if (cfg != null) {
                LOG.trace("Oppslag token X konfig for {} OK", req.getHost());
            } else {
                LOG.trace("Oppslag token X konfig for {} fant ingenting", req.getHost());
            }
            return cfg;
        };
    }

    @Bean
    public List<RetryListener> retryListeners() {
        return singletonList(new RetryListener() {

            @Override
            public <T, E extends Throwable> void onError(RetryContext ctx, RetryCallback<T, E> cb,
                    Throwable throwable) {
                LOG.warn("Metode {} kastet exception {} for {}. gang",
                        ctx.getAttribute(NAME), throwable, ctx.getRetryCount());
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext ctx, RetryCallback<T, E> cb, Throwable t) {
                if (t != null) {
                    LOG.warn("Metode {} avslutter ikke-vellykket retry etter {}. forsøk grunnet {}",
                            ctx.getAttribute(NAME), ctx.getRetryCount(), t.toString(), t);
                } else {
                    if (ctx.getRetryCount() > 0) {
                        LOG.info("Metode {} avslutter vellykket retry etter {}. forsøk",
                                ctx.getAttribute(NAME), ctx.getRetryCount());
                    }
                }
            }

            @Override
            public <T, E extends Throwable> boolean open(RetryContext ctx, RetryCallback<T, E> cb) {
                var labelField = ReflectionUtils.findField(cb.getClass(), "val$label");
                ReflectionUtils.makeAccessible(labelField);
                String metode = (String) ReflectionUtils.getField(labelField, cb);
                if (ctx.getRetryCount() > 0) {
                    LOG.info("Metode {} gjør retry for {}. gang", metode, ctx.getRetryCount());
                }
                return true;
            }
        });

    }

    @Bean
    public Bøtte tmpBøtte(
            @Value("${mellomlagring.tmp.navn:mellomlagring}") String navn,
            @Value("${mellomlagring.tmp.levetid:1d}") Duration levetid,
            @Value("${mellomlagring.tmp.enabled:true}") boolean enabled) {
        return new Bøtte(navn, levetid, enabled);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods(POST.name(), GET.name(), OPTIONS.name(), DELETE.name())
                .allowCredentials(true)
                .exposedHeaders(LOCATION)
                .allowedHeaders(FNR, CONTENT_TYPE)
                .allowedOrigins(allowedOrigins);
    }
}
