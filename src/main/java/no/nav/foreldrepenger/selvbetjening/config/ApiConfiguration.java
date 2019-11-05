package no.nav.foreldrepenger.selvbetjening.config;

import static java.util.Collections.singletonList;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Bøtte.SØKNAD;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Bøtte.TMP;
import static no.nav.foreldrepenger.selvbetjening.util.Constants.FNR;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.retry.RetryContext.NAME;

import java.net.URI;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.common.collect.ImmutableMap;

import no.nav.foreldrepenger.selvbetjening.interceptors.client.ZoneCrossingAware;
import no.nav.foreldrepenger.selvbetjening.interceptors.client.ZoneCrossingAwareClientInterceptor;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Bøtte;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.MellomlagringKrypto;
import no.nav.foreldrepenger.selvbetjening.util.conditionals.ConditionalOnK8s;

@Configuration
public class ApiConfiguration implements WebMvcConfigurer {
    private static final Logger LOG = LoggerFactory.getLogger(ApiConfiguration.class);

    private final String[] allowedOrigins;

    public ApiConfiguration(@Value("${allowed.origins}") String[] allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @Bean
    public RestOperations restTemplate(ClientHttpRequestInterceptor... interceptors) {
        LOG.info("Registrerer interceptorer {}", Arrays.toString(interceptors));
        return new RestTemplateBuilder()
                .interceptors(interceptors)
                .build();
    }

    @Bean
    public ClientHttpRequestInterceptor zoneCrossingAwareRequestInterceptor(ZoneCrossingAware... zoneCrossers) {
        var builder = ImmutableMap.<URI, String>builder();
        Arrays.stream(zoneCrossers)
                .forEach(c -> builder.put(c.zoneCrossingUri(), c.getKey()));
        return new ZoneCrossingAwareClientInterceptor(builder.build());
    }

    @Bean
    public List<RetryListener> retryListeners() {
        Logger log = LoggerFactory.getLogger(getClass());

        return singletonList(new RetryListenerSupport() {

            @Override
            public <T, E extends Throwable> void onError(RetryContext ctx, RetryCallback<T, E> callback,
                    Throwable t) {
                log.warn("Metode {} kastet exception {} for {}. gang",
                        ctx.getAttribute(NAME), t.toString(), ctx.getRetryCount());
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext ctx, RetryCallback<T, E> callback,
                    Throwable t) {
                if (t != null) {
                    log.warn("Metode {} avslutter ikke-vellykket retry etter {}. forsøk", ctx.getAttribute(NAME),
                            ctx.getRetryCount());
                } else {
                    log.info("Metode {} avslutter vellykket retry etter {}. forsøk", ctx.getAttribute(NAME),
                            ctx.getRetryCount());
                }
            }

            @Override
            public <T, E extends Throwable> boolean open(RetryContext ctx, RetryCallback<T, E> callback) {
                var labelField = ReflectionUtils.findField(callback.getClass(), "val$label");
                ReflectionUtils.makeAccessible(labelField);
                String label = (String) ReflectionUtils.getField(labelField, callback);
                log.info("Metode {} initierer retry", label);
                return true;
            }
        });
    }

    @Bean
    @ConditionalOnK8s
    @Qualifier(SØKNAD)
    public Bøtte søknadsBøtte(
            @Value("${mellomlagring.søknad.navn:foreldrepengesoknad}") String navn,
            @Value("${mellomlagring.søknad.levetid:365d}") Duration levetid,
            @Value("${mellomlagring.søknad.enabled:true}") boolean enabled) {
        return new Bøtte(navn, levetid, enabled);
    }

    @Bean
    @Qualifier(TMP)
    @ConditionalOnK8s
    public Bøtte tmpBøtte(
            @Value("${mellomlagring.tmp.navn:mellomlagring}") String navn,
            @Value("${mellomlagring.tmp.levetid:1d}") Duration levetid,
            @Value("${mellomlagring.tmp.enabled:true}") boolean enabled) {
        return new Bøtte(navn, levetid, enabled);
    }

    @Bean
    public MellomlagringKrypto krypto(@Value("${storage.passphrase}") String passPhrase) {
        return new MellomlagringKrypto(passPhrase);
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
