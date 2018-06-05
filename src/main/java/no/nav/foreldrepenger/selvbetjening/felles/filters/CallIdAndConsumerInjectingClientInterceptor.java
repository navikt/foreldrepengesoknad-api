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

import no.nav.foreldrepenger.selvbetjening.felles.util.UUIDCallIdGenerator;

@Component
@Order(1)
public class CallIdAndConsumerInjectingClientInterceptor implements ClientHttpRequestInterceptor {

    @Inject
    private UUIDCallIdGenerator generator;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.add(generator.getCallIdKey(), MDC.get(generator.getCallIdKey()));
        headers.add("Nav-Consumer-Id", "foreldrepenger-selvbetjening");
        return execution.execute(request, body);
    }
}
