package no.nav.foreldrepenger.selvbetjening.http;

import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.CONFIDENTIAL;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

public abstract class AbstractRestConnection implements PingEndpointAware, Togglable {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractRestConnection.class);

    private final RestOperations operations;

    protected AbstractRestConnection(RestOperations operations) {
        this.operations = operations;
    }

    public <T> T getForObject(URI uri, Class<T> responseType) {
        return getForObject(uri, responseType, true);
    }

    public <T> T getForObject(URI uri, Class<T> responseType, boolean throwOnNotFound) {
        try {
            if (!isEnabled()) {
                LOG.info("Service er ikke aktiv, GETer ikke fra {}", uri);
                return null;
            }
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

    public <T> T postForObject(URI uri, Object payload, Class<T> responseType) {
        if (!isEnabled()) {
            LOG.info("Service er ikke aktiv, POSTer ikke til {}", uri);
            return null;
        }
        return operations.postForObject(uri, payload, responseType);
    }

    public <T> T putForObject(URI uri, Object payload, Class<T> responseType) {
        if (!isEnabled()) {
            LOG.info("Service er ikke aktiv, PUTer ikke til {}", uri);
            return null;
        }
        return operations.exchange(RequestEntity.put(uri).body(payload), responseType).getBody();
    }

    @Override
    public String name() {
        return getClass().getSimpleName();
    }

    @Override
    public String ping() {
        return getForObject(pingURI(), String.class);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [operations=" + operations + "]";
    }
}
