package no.nav.foreldrepenger.selvbetjening.tjeneste;

import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.CONFIDENTIAL;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestOperations;

public abstract class AbstractRestConnection implements Pingable {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractRestConnection.class);

    protected abstract boolean isEnabled();

    private final RestOperations operations;

    public AbstractRestConnection(RestOperations operations) {
        this.operations = operations;
    }

    public String ping() {
        return getForObject(pingURI(), String.class);
    }

    protected <T> T getForObject(URI uri, Class<T> responseType) {
        return getForObject(uri, responseType, true);
    }

    @Retryable(value = { HttpServerErrorException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    protected <T> T getForObject(URI uri, Class<T> responseType, boolean throwOnNotFound) {
        try {
            T respons = operations.getForObject(uri, responseType);
            if (respons != null) {
                LOG.trace(CONFIDENTIAL, "Respons: {}", respons);
            }
            return respons;
        } catch (HttpClientErrorException e) {
            if (!throwOnNotFound && NOT_FOUND.equals(e.getStatusCode())) {
                LOG.info("Fant intet objekt på {}, returnerer null", uri);
                return null;
            }
            throw e;
        }
    }

    @Retryable(value = { HttpServerErrorException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    protected <T> T postForObject(URI uri, Object payload, Class<T> responseType) {
        return operations.postForObject(uri, payload, responseType);
    }

    @Retryable(value = { HttpServerErrorException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    protected <T> T putForObject(URI uri, Object payload, Class<T> responseType) {
        if (!isEnabled()) {
            LOG.info("Service er ikke aktiv, PUTer ikke til {}", uri);
            return null;
        }
        return operations.exchange(RequestEntity.put(uri).body(payload), responseType).getBody();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [operations=" + operations + "]";
    }
}
