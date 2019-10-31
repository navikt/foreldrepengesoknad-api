package no.nav.foreldrepenger.selvbetjening.tjeneste;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

import no.nav.foreldrepenger.selvbetjening.util.Pair;

public class UriUtil {

    public static URI uri(String base, String path) {
        return uri(URI.create(base), path);
    }

    public static URI uri(URI base, String path) {
        return uri(base, path, null);
    }

    public static URI uri(URI base, String path, HttpHeaders queryParams) {
        return builder(base, path, queryParams)
                .build()
                .toUri();
    }

    public static HttpHeaders queryParams(String key, String value) {
        return queryParams(Pair.of(key, value));
    }

    public static HttpHeaders queryParams(Pair<String, Object>... pairs) {
        HttpHeaders queryParams = new HttpHeaders();
        for (Pair<String, Object> pair : pairs) {
            queryParams.add(pair.getFirst(), pair.getSecond().toString());
        }
        return queryParams;
    }

    public static UriComponentsBuilder builder(URI base, String path, HttpHeaders queryParams) {
        return UriComponentsBuilder
                .fromUri(base)
                .pathSegment(path)
                .queryParams(queryParams);
    }
}
