package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import static no.nav.foreldrepenger.selvbetjening.felles.util.EnvUtil.CONFIDENTIAL;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import no.nav.foreldrepenger.selvbetjening.felles.error.NotFoundException;
import no.nav.foreldrepenger.selvbetjening.felles.error.RemoteUnavailableException;
import no.nav.foreldrepenger.selvbetjening.felles.error.UnauthenticatedException;
import no.nav.foreldrepenger.selvbetjening.felles.error.UnauthorizedException;
import no.nav.foreldrepenger.selvbetjening.felles.util.EnvUtil;
import no.nav.foreldrepenger.selvbetjening.felles.util.TokenHandler;
import no.nav.foreldrepenger.selvbetjening.tjeneste.Pingable;

public abstract class AbstractRestConnection implements EnvironmentAware, Pingable {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRestConnection.class);

    protected final RestTemplate template;
    private final TokenHandler tokenHandler;
    private Environment env;

    protected abstract boolean isEnabled();

    public AbstractRestConnection(RestTemplate template, TokenHandler tokenHandler) {
        this.template = template;
        this.tokenHandler = tokenHandler;
    }

    protected String ping(URI uri) {
        return getForEntity(uri, String.class);
    }

    protected static HttpHeaders queryParams(String key, String value) {
        HttpHeaders queryParams = new HttpHeaders();
        queryParams.add(key, value);
        return queryParams;
    }

    protected <T> T getForObject(URI uri, Class<T> responseType) {
        return getForObject(uri, responseType, false);
    }

    protected <T> ResponseEntity<T> postForEntity(URI uri, HttpEntity<?> payload, Class<T> responseType) {
        try {
            ResponseEntity<T> respons = template.postForEntity(uri, payload, responseType);
            LOG.trace("Fikk respons OK for {}", uri);
            if (respons.hasBody()) {
                LOG.trace(CONFIDENTIAL, "Respons: {}", respons.getBody());
            }
            return respons;
        } catch (HttpStatusCodeException e) {
            HttpStatus code = e.getStatusCode();
            LOG.warn("Kunne ikke poste entitet til {}, status kode var {}", uri, code, e);
            switch (code) {
            case UNAUTHORIZED:
                throw new UnauthorizedException(e);
            case FORBIDDEN:
                throw new UnauthenticatedException(tokenHandler.getExp(), e);
            default:
                throw new RemoteUnavailableException(e);
            }
        } catch (RestClientException e) {
            throw new RemoteUnavailableException(e);
        }
    }

    protected <T> T getForEntity(URI uri, Class<T> responseType) {
        try {
            ResponseEntity<T> respons = template.getForEntity(uri, responseType);
            LOG.trace("Fikk respons OK for {}", uri);
            if (respons.hasBody()) {
                LOG.trace(CONFIDENTIAL, "Respons: {}", respons.getBody());
            }
            return respons.getBody();
        } catch (HttpStatusCodeException e) {
            HttpStatus code = e.getStatusCode();
            LOG.warn("Fant ingen entitet på {}, status kode var {}", uri, code, e);
            switch (code) {
            case NOT_FOUND:
                throw new NotFoundException(e);
            case UNAUTHORIZED:
                throw new UnauthorizedException(e);
            case FORBIDDEN:
                throw new UnauthenticatedException(tokenHandler.getExp(), e);
            default:
                throw new RemoteUnavailableException(e);
            }
        } catch (RestClientException e) {
            LOG.warn("Fant ingen entitet på {}", uri, e);
            throw new RemoteUnavailableException(e);
        }
    }

    protected <T> T getForObject(URI uri, Class<T> responseType, boolean doThrow) {
        try {
            T respons = template.getForObject(uri, responseType);
            LOG.trace("Fikk respons OK for {}", uri);
            if (respons != null) {
                LOG.trace(CONFIDENTIAL, "Respons: {}", respons);
            }
            return respons;
        } catch (HttpStatusCodeException e) {
            HttpStatus code = e.getStatusCode();
            LOG.warn("Fant intet objekt på {}, status kode var {}", uri, code, e);
            switch (code) {
            case NOT_FOUND:
                if (doThrow) {
                    LOG.trace("kaster NotFoundException videre");
                    throw new NotFoundException(e);
                }
                LOG.trace("Returnerer null");
                return null;
            case UNAUTHORIZED:
                throw new UnauthorizedException(e);
            case FORBIDDEN:
                throw new UnauthenticatedException(tokenHandler.getExp(), e);
            default:
                throw new RemoteUnavailableException(e);
            }
        } catch (RestClientException e) {
            LOG.warn("Fant intet objekt på {}", uri, e);
            throw new RemoteUnavailableException(uri, e);
        }
    }

    protected boolean isDevOrPreprod() {
        return EnvUtil.isDevOrPreprod(env);
    }

    protected static URI uri(URI base, String path) {
        return uri(base, path, null);
    }

    protected static URI uri(URI base, String path, HttpHeaders queryParams) {
        return builder(base, path)
                .queryParams(queryParams)
                .build()
                .toUri();
    }

    private static UriComponentsBuilder builder(URI base, String path) {
        return UriComponentsBuilder
                .fromUri(base)
                .pathSegment(path);
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }
}
