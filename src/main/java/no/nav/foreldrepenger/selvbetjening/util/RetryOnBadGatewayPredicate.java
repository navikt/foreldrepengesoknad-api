package no.nav.foreldrepenger.selvbetjening.util;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.function.Predicate;

import org.springframework.web.client.HttpServerErrorException;

public class RetryOnBadGatewayPredicate implements Predicate<Throwable> {
    @Override
    public boolean test(Throwable throwable) {
        if (throwable instanceof HttpServerErrorException) {
            return HttpServerErrorException.class.cast(throwable).getStatusCode().equals(BAD_REQUEST);
        }
        return true;
    }
}
