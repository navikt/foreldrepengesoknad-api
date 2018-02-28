package no.nav.foreldrepenger.selvbetjening.service.ping;

import static org.slf4j.LoggerFactory.getLogger;

import java.net.URI;

import org.slf4j.Logger;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractPingService implements Pinger {

    private static final Logger LOG = getLogger(AbstractPingService.class);

    protected final RestTemplate template;
    protected final URI baseUri;

    protected abstract URI pingURI(String message);

    public AbstractPingService(RestTemplate template, URI baseUri) {
        this.template = template;
        this.baseUri = baseUri;
    }

    @Override
    public String ping(String message) {
        URI uri = pingURI(message);
        LOG.info("Pinging remote {}", uri);
        return template.getForObject(uri, String.class);
    }

    @Override
    public URI baseUri() {
        return baseUri;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [template=" + template + ", baseUri=" + baseUri + "]";
    }
}
