package no.nav.foreldrepenger.selvbetjening.tjeneste;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;

import no.nav.foreldrepenger.selvbetjening.error.NotFoundException;
import no.nav.foreldrepenger.selvbetjening.error.UnauthenticatedException;
import no.nav.foreldrepenger.selvbetjening.error.UnauthorizedException;
import no.nav.foreldrepenger.selvbetjening.util.TokenHelper;

public class StatusCodeConvertingResponseErrorHandler extends DefaultResponseErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(StatusCodeConvertingResponseErrorHandler.class);
    private final TokenHelper tokenHandler;

    public StatusCodeConvertingResponseErrorHandler(TokenHelper tokenHandler) {
        this.tokenHandler = tokenHandler;
    }

    @Override
    protected void handleError(ClientHttpResponse res, HttpStatus code) throws IOException {
        LOG.info("Håndterer feilkode {}", code);
        switch (code) {
        case NOT_FOUND:
            throw new NotFoundException(res.getStatusText(), new HttpClientErrorException(code));
        case UNAUTHORIZED:
            throw new UnauthorizedException(res.getStatusText(), new HttpClientErrorException(code));
        case FORBIDDEN:
            throw new UnauthenticatedException(res.getStatusText(), tokenHandler.getExp(),
                    new HttpClientErrorException(code));
        default:
            super.handleError(res, code);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [tokenHandler=" + tokenHandler + "]";
    }
}
