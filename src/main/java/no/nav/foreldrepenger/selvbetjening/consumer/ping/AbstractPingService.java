package no.nav.foreldrepenger.selvbetjening.consumer.ping;

import java.net.URI;

import org.slf4j.Logger;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractPingService implements Pinger {

    protected final RestTemplate template;
    private final URI baseUri;

    protected abstract URI pingURI(String message);

    protected abstract Logger logger();

    public AbstractPingService(RestTemplate template, URI baseUri) {
        this.template = template;
        this.baseUri = baseUri;
    }

    @Override
    public String ping(String message) {
        URI uri = pingURI(message);
        logger().info("Pinging remote {}", uri);
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
