package no.nav.foreldrepenger.selvbetjening.http;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartException;


@Retryable(
    include = {RestClientException.class, MultipartException.class, HttpClientErrorException.BadRequest.class, },
    exclude = {
        HttpClientErrorException.NotFound.class,
        HttpClientErrorException.Forbidden.class,
        HttpClientErrorException.BadRequest.class,
        HttpClientErrorException.Unauthorized.class,
        HttpServerErrorException.class,
        HttpClientErrorException.NotFound.class,
        HttpClientErrorException.Forbidden.class,
        HttpClientErrorException.Unauthorized.class }, maxAttemptsExpression = "#{${rest.retry.attempts:3}}", backoff = @Backoff(delayExpression = "#{${rest.retry.delay:1000}}"))

public interface RetryAware {

}
