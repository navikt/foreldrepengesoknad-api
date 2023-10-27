package no.nav.foreldrepenger.selvbetjening.http;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartException;


@Retryable(
    include = {
        HttpServerErrorException.class,
        MultipartException.class },
    maxAttemptsExpression = "#{${rest.retry.attempts:2}}",
    backoff = @Backoff(delayExpression = "#{${rest.retry.delay:1000}}")
)
public interface RetryAware {

}
