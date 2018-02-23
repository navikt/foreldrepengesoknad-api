package no.nav.foreldrepenger.selvbetjening.rest;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyInsertingClientInterceptor implements ClientHttpRequestInterceptor {

    private static final String KEY = "x-nav-apiKey";

    private final Map<URI, String> apiKeys;

    public ApiKeyInsertingClientInterceptor(Map<URI, String> apiKeys) {
        this.apiKeys = apiKeys;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        // HttpHeaders headers = request.getHeaders();
        // headers.add(KEY, apiKey(request.getURI()));
        return execution.execute(request, body);
    }

    private String apiKey(URI uri) {

        // TODO Auto-generated method stub
        return null;
    }
}