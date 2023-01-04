package no.nav.foreldrepenger.selvbetjening.http.interceptors;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

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

    private static void log(HttpRequest request, HttpStatus code, long ms) {
        if (hasError(code)) {
            LOG.warn("{} - {} - ({}). Dette tok {}ms", request.getMethodValue(), request.getURI().getPath(), code, ms);
        } else {
            LOG.info("{} - {} - ({}). Dette tok {}ms", request.getMethodValue(), request.getURI().getPath(), code, ms);
        }
    }

    private static boolean hasError(HttpStatus code) {
        return code.series().equals(CLIENT_ERROR) || code.series().equals(SERVER_ERROR);
    }
}
