package no.nav.foreldrepenger.selvbetjening.http;

import java.net.URI;

public abstract class AbstractConfig {

    protected abstract URI pingURI();

    private final URI baseUri;
    private final boolean enabled;

    protected AbstractConfig(URI baseUri, boolean enabled) {
        this.enabled = enabled;
        this.baseUri = baseUri;
    }

    public URI getBaseUri() {
        return baseUri;
    }

    public boolean isEnabled() {
        return enabled;
    }

}
