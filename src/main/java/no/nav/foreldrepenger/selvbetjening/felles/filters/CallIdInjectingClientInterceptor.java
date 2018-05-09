package no.nav.foreldrepenger.selvbetjening.felles.filters;

import java.io.IOException;

import javax.inject.Inject;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.felles.util.CallIdGenerator;

@Component
@Order(1)
public class CallIdInjectingClientInterceptor implements ClientHttpRequestInterceptor {

    @Inject
    private CallIdGenerator generator;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.add(generator.getKey(), MDC.get(generator.getKey()));
        return execution.execute(request, body);
    }
}
