package no.nav.foreldrepenger.selvbetjening.rest;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyInsertingClientInterceptor implements ClientHttpRequestInterceptor {

    private static final String KEY = "x-nav-apiKey";

    private final String apiKey;

    public ApiKeyInsertingClientInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.add(KEY, apiKey);
        return execution.execute(request, body);
    }
}