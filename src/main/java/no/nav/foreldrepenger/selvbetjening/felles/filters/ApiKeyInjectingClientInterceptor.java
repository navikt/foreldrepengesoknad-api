package no.nav.foreldrepenger.selvbetjening.felles.filters;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class ApiKeyInjectingClientInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOG = getLogger(ApiKeyInjectingClientInterceptor.class);

    private final Map<URI, String> apiKeys;
    private final String headerKey;

    public ApiKeyInjectingClientInterceptor(@Value("${apikeys.key:x-nav-apiKey}") String headerKey, Map<URI, String> apiKeys) {
        this.headerKey = headerKey;
        this.apiKeys = apiKeys;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        URI destination = request.getURI();
        Optional<String> apiKey = apiKeyFor(destination);
        if (apiKey.isPresent()) {
            LOG.trace("Injisert API-key som header {} for {}", headerKey, destination);
            request.getHeaders().add(headerKey, apiKey.get());
        } else {
            LOG.trace("Ingen API-key ble funnet for {} (sjekket {} konfigurasjoner)", destination, apiKeys.values().size());
        }
        return execution.execute(request, body);
    }

    private Optional<String> apiKeyFor(URI uri) {
        return apiKeys.entrySet().stream()
                .filter(s -> uri.toString().startsWith(s.getKey().toString()))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [apiKeys=" + apiKeys.keySet() + ", headerKey=" + headerKey + "]";
    }

}
