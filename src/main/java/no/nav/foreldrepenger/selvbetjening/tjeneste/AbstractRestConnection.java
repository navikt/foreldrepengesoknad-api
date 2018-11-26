package no.nav.foreldrepenger.selvbetjening.tjeneste;

import java.net.URI;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.selvbetjening.error.NotFoundException;
import no.nav.foreldrepenger.selvbetjening.util.EnvUtil;

public abstract class AbstractRestConnection implements EnvironmentAware {

    protected abstract boolean isEnabled();

    private final RestOperations operations;
    private Environment env;

    public AbstractRestConnection(RestOperations operations) {
        this.operations = operations;
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    protected String ping(URI uri) {
        return getForObject(uri, String.class);
    }

    protected <T> T getForObject(URI uri, Class<T> responseType) {
        return getForObject(uri, responseType, false);
    }

    protected <T> T getForObject(URI uri, Class<T> responseType, boolean doThrow) {
        try {
            return operations.getForObject(uri, responseType);
        } catch (NotFoundException e) {
            if (doThrow) {
                throw e;
            }
            return null;
        }
    }

    protected <T> ResponseEntity<T> postForEntity(URI uri, HttpEntity<?> payload, Class<T> responseType) {
        return operations.postForEntity(uri, payload, responseType);
    }

    protected boolean isDevOrPreprod() {
        return EnvUtil.isDevOrPreprod(env);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [operations=" + operations + "]";
    }
}
