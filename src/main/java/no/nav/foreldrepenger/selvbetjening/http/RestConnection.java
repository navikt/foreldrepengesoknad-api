package no.nav.foreldrepenger.selvbetjening.http;

import java.net.URI;

public interface RestConnection extends PingEndpointAware, Togglable {

    <T> T getForObject(URI uri, Class<T> responseType);

    <T> T getForObject(URI uri, Class<T> responseType, boolean throwOnNotFound);

    <T> T postForObject(URI uri, Object payload, Class<T> responseType);

    <T> T putForObject(URI uri, Object payload, Class<T> responseType);

}