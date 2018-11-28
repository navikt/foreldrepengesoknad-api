package no.nav.foreldrepenger.selvbetjening.tjeneste;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.selvbetjening.error.NotFoundException;
import no.nav.foreldrepenger.selvbetjening.util.EnvUtil;

public abstract class AbstractRestConnection implements EnvironmentAware {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractRestConnection.class);

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

    protected abstract URI pingURI();

    public String ping() {
        return getForObject(pingURI(), String.class);
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

    protected <T> T postForObject(URI uri, Object payload, Class<T> responseType) {
        return operations.postForObject(uri, payload, responseType);
    }

    protected boolean isDevOrPreprod() {
        return EnvUtil.isDevOrPreprod(env);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [operations=" + operations + "]";
    }
}
