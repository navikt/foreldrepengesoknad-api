package no.nav.foreldrepenger.selvbetjening.http;

import static no.nav.boot.conditionals.EnvUtil.CONFIDENTIAL;
import static no.nav.foreldrepenger.selvbetjening.util.StringUtils.escapeHtml;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

public abstract class AbstractRestConnection {

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
            var respons = operations.getForObject(uri, responseType);
            if (respons != null) {
                LOG.trace(CONFIDENTIAL, "Respons: {}", respons);
            }
            return respons;
        } catch (HttpClientErrorException e) {
            if (!throwOnNotFound && NOT_FOUND.equals(e.getStatusCode())) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Fant intet objekt på {}, returnerer null", escapeHtml(uri));
                }
                return null;
            }
            throw e;
        }
    }

    public <T> T postForObject(URI uri, Object payload, Class<T> responseType) {
        return operations.postForObject(uri, payload, responseType);
    }

    public <T> T putForObject(URI uri, Object payload, Class<T> responseType) {
        return operations.exchange(RequestEntity.put(uri).body(payload), responseType).getBody();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [operations=" + operations + "]";
    }
}
