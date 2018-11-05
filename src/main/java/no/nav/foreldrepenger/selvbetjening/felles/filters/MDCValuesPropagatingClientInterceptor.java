package no.nav.foreldrepenger.selvbetjening.felles.filters;

import static no.nav.foreldrepenger.selvbetjening.felles.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.selvbetjening.felles.Constants.NAV_CONSUMER_ID;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class MDCValuesPropagatingClientInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(MDCValuesPropagatingClientInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        propagateIfSet(request, NAV_CALL_ID, NAV_CONSUMER_ID);
        return execution.execute(request, body);
    }

    private static void propagateIfSet(HttpRequest request, String... keys) {
        for (String key : keys) {
            String value = MDC.get(key);
            if (value != null) {
                LOG.trace("Propagating {}", key);
                request.getHeaders().add(key, value);
            }
            else {
                LOG.trace("NOT Propagating {}", key);
            }
        }
    }
}
