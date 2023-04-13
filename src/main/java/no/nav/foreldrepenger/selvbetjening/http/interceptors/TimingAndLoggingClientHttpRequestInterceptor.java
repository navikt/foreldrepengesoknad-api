package no.nav.foreldrepenger.selvbetjening.http.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Component
public class TimingAndLoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(TimingAndLoggingClientHttpRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        var start = Instant.now();
        ClientHttpResponse respons = execution.execute(request, body);
        var finish = Instant.now();
        var ms = Duration.between(start, finish).toMillis();
        log(request, respons.getStatusCode(), ms);
        return respons;
    }

    private static void log(HttpRequest request, HttpStatusCode code, long ms) {
        if (hasError(code)) {
            LOG.warn("{} - {} - ({}). Dette tok {}ms", request.getMethod(), request.getURI().getPath(), code, ms);
        } else {
            LOG.info("{} - {} - ({}). Dette tok {}ms", request.getMethod(), request.getURI().getPath(), code, ms);
        }
    }

    private static boolean hasError(HttpStatusCode code) {
        return code.is4xxClientError() || code.is5xxServerError();
    }
}
