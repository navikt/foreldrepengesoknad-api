package no.nav.foreldrepenger.selvbetjening.tjeneste;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.io.IOException;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
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

        LOG.info("Eksekverer {} - {}", request.getMethodValue(), request.getURI());
        StopWatch timer = new StopWatch();
        timer.start();
        ClientHttpResponse respons = execution.execute(request, body);
        timer.stop();
        LOG.info("{} - {}. Dette tok {}ms", request.getMethodValue(), request.getURI(), timer.getTime(MILLISECONDS));
        return respons;
    }
}