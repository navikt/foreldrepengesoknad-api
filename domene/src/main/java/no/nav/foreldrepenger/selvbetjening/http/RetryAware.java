package no.nav.foreldrepenger.selvbetjening.http;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartException;

import no.nav.foreldrepenger.selvbetjening.error.IdMismatchException;
import no.nav.security.token.support.core.exceptions.JwtTokenInvalidClaimException;
import no.nav.security.token.support.spring.validation.interceptor.JwtTokenUnauthorizedException;


@Retryable(
    include = {
        RestClientException.class,
        MultipartException.class },
    exclude = {
        IdMismatchException.class,
        JwtTokenUnauthorizedException.class,
        JwtTokenInvalidClaimException.class,
        HttpClientErrorException.class},
    maxAttemptsExpression = "#{${rest.retry.attempts:3}}",
    backoff = @Backoff(delayExpression = "#{${rest.retry.delay:1000}}"))
public interface RetryAware {

}
