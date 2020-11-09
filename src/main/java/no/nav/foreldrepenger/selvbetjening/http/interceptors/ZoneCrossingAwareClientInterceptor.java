package no.nav.foreldrepenger.selvbetjening.http.interceptors;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.selvbetjening.util.Constants.X_NAV_API_KEY;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class ZoneCrossingAwareClientInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOG = getLogger(ZoneCrossingAwareClientInterceptor.class);

    private final List<ZoneCrossingAware> zoneCrossers;

    public ZoneCrossingAwareClientInterceptor(ZoneCrossingAware... zoneCrossers) {
        this.zoneCrossers = Arrays.asList(zoneCrossers);
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        Optional<String> apiKey = apiKeyFor(request.getURI());
        if (apiKey.isPresent()) {
            LOG.trace("Injisert API-key som header {} for {}", X_NAV_API_KEY, request.getURI());
            request.getHeaders().add(X_NAV_API_KEY, apiKey.get());
        } else {
            LOG.trace("Ingen API-key ble funnet for {} (sjekket {} konfigurasjoner)", request.getURI(),
                    zoneCrossers.size());
        }
        return execution.execute(request, body);
    }

    private Optional<String> apiKeyFor(URI uri) {
        return zoneCrossers.stream()
                .filter(z -> uri.toString().startsWith(z.zoneCrossingUri().toString()))
                .map(ZoneCrossingAware::getKey)
                .findFirst();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [zoneCrossers=" + zoneCrossers.stream().map(ZoneCrossingAware::zoneCrossingUri).collect(toList()) + "]";
    }

}
