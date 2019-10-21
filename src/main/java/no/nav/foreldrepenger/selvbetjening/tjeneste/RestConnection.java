package no.nav.foreldrepenger.selvbetjening.tjeneste;

import java.net.URI;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpServerErrorException;

@Retryable(value = { HttpServerErrorException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
public interface RestConnection extends Pingable {
    <T> T getForObject(URI uri, Class<T> responseType);

    <T> T getForObject(URI uri, Class<T> responseType, boolean throwOnNotFound);

    <T> T postForObject(URI uri, Object payload, Class<T> responseType);

    <T> T putForObject(URI uri, Object payload, Class<T> responseType);

}
