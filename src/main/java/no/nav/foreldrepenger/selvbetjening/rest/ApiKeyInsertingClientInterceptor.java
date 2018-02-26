package no.nav.foreldrepenger.selvbetjening.rest;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyInsertingClientInterceptor implements ClientHttpRequestInterceptor {

    private static final String KEY = "x-nav-apiKey";

    private final Map<URI, String> apiKeys;
    private static final Logger LOG = getLogger(ApiKeyInsertingClientInterceptor.class);

    public ApiKeyInsertingClientInterceptor(Map<URI, String> apiKeys) {
        this.apiKeys = apiKeys;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        String apiKey = apiKeyFor(request.getURI());
        if (apiKey != null) {
            request.getHeaders().add(KEY, apiKey);
        } else {
            LOG.warn("Ingen api key ble funnet for {}", request.getURI());
        }
        return execution.execute(request, body);
    }

    private String apiKeyFor(URI uri) {
        return Optional.ofNullable(apiKeys.entrySet().stream()
                .filter(s -> uri.toString().startsWith(s.getKey().toString()))
                .map(s -> s.getValue())
                .collect(Collectors.reducing((a, b) -> null)))
                .map(s -> s.get()).orElse(null);
    }
}