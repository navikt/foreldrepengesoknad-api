package no.nav.foreldrepenger.selvbetjening.interceptors.client;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

import java.io.IOException;

import org.apache.commons.lang3.time.StopWatch;
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

        StopWatch timer = new StopWatch();
        timer.start();
        ClientHttpResponse respons = execution.execute(request, body);
        timer.stop();
        log(request, respons.getStatusCode(), timer);
        return respons;
    }

    private static void log(HttpRequest request, HttpStatus code, StopWatch timer) {
        if (hasError(code)) {
            LOG.warn("{} - {} - ({}). Dette tok {}ms", request.getMethodValue(), request.getURI().getPath(),
                    code, timer.getTime(MILLISECONDS));
        }
        else {
            LOG.info("{} - {} - ({}). Dette tok {}ms", request.getMethodValue(), request.getURI().getPath(),
                    code, timer.getTime(MILLISECONDS));
        }
    }

    private static boolean hasError(HttpStatus code) {
        return code.series() == CLIENT_ERROR || code.series() == SERVER_ERROR;
    }
}