package no.nav.foreldrepenger.selvbetjening.tjeneste;

import java.net.URI;

import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

import no.nav.foreldrepenger.selvbetjening.util.Pair;

@ConstructorBinding
public abstract class AbstractConfig {

    protected abstract URI pingURI();

    private final URI uri;
    private final boolean enabled;

    public AbstractConfig(URI uri, boolean enabled) {
        this.enabled = enabled;
        this.uri = uri;
    }

    public URI getUri() {
        return uri;
    }

    public boolean isEnabled() {
        return enabled;
    }

    protected URI uri(String base, String path) {
        return uri(URI.create(base), path);

    }

    protected URI uri(URI base, String path) {
        return uri(base, path, null);
    }

    protected URI uri(URI base, String path, HttpHeaders queryParams) {
        return builder(base, path, queryParams)
                .build()
                .toUri();
    }

    protected static HttpHeaders queryParams(String key, String value) {
        return queryParams(Pair.of(key, value));
    }

    protected static HttpHeaders queryParams(Pair<String, Object>... pairs) {
        HttpHeaders queryParams = new HttpHeaders();
        for (Pair<String, Object> pair : pairs) {
            queryParams.add(pair.getFirst(), pair.getSecond().toString());
        }
        return queryParams;
    }

    private static UriComponentsBuilder builder(URI base, String path, HttpHeaders queryParams) {
        return UriComponentsBuilder
                .fromUri(base)
                .pathSegment(path)
                .queryParams(queryParams);
    }

}
