package no.nav.foreldrepenger.selvbetjening.felles.filters;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class MDCValuesPropagatingClientInterceptor implements ClientHttpRequestInterceptor {

    public static final String NAV_CONSUMER_ID = "Nav-Consumer-Id";
    public static final String NAV_CALL_ID = "Nav-CallId";

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
                request.getHeaders().add(key, value);
            }
        }
    }
}
