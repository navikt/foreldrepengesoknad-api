package no.nav.foreldrepenger.selvbetjening.config;

import java.net.URI;

public class AbstractConfig implements GatewayAware {
    private boolean enabled;
    private final URI uri;

    private String apiKey;

    public AbstractConfig(URI uri) {
        this.uri = uri;
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    protected void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean isEnabled() {
        return enabled;
    }

    protected void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
