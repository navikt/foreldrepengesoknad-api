package no.nav.foreldrepenger.selvbetjening.util;

import static no.nav.foreldrepenger.common.util.StringUtil.taint;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

public final class URIUtil {
    private URIUtil() {
    }

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

    public static UriComponentsBuilder builder(URI base, String path, HttpHeaders queryParams) {
        return UriComponentsBuilder
                .fromUri(base)
                .pathSegment(path)
                .queryParams(queryParams);
    }

    public static HttpHeaders queryParam(String key, String value) {
        var queryParams = new HttpHeaders();
        queryParams.add(key, taint(value));
        return queryParams;
    }

    public static HttpHeaders queryParams(String key, String value, String key1, String value1) {
        var queryParams = new HttpHeaders();
        queryParams.add(key, taint(value));
        queryParams.add(key1, taint(value1));
        return queryParams;
    }

    public static URI uri(URI base, HttpHeaders queryParams) {
        return uri(base, null, queryParams);
    }
}
