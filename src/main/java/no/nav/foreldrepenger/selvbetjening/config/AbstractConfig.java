package no.nav.foreldrepenger.selvbetjening.config;

import java.net.URI;

public class AbstractConfig implements GatewayAware {
    private boolean enabled;
    private final URI uri;

    private final String apikey;

    public AbstractConfig(URI uri, String apikey) {
        this.uri = uri;
        this.apikey = apikey;
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public String getApikey() {
        return apikey;
    }

    public boolean isEnabled() {
        return enabled;
    }

    protected void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
