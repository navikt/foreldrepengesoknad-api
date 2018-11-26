package no.nav.foreldrepenger.selvbetjening.util;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

public final class URIUtils {

    private URIUtils() {

    }

    public static URI uri(URI base, String path) {
        return uri(base, path, null);
    }

    public static URI uri(URI base, String path, HttpHeaders queryParams) {
        return builder(base, path, queryParams)
                .build()
                .toUri();
    }

    public static UriComponentsBuilder builder(URI base, String path, HttpHeaders queryParams) {
        return UriComponentsBuilder
                .fromUri(base)
                .pathSegment(path)
                .queryParams(queryParams);

    }

    public static HttpHeaders queryParams(String key, String value) {
        HttpHeaders queryParams = new HttpHeaders();
        queryParams.add(key, value);
        return queryParams;
    }

}
