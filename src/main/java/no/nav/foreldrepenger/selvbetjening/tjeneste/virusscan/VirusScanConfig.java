package no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "no.nav.foreldrepenger.selvbetjening.api.virus")
@Configuration
class VirusScanConfig {

    private static final URI DEFAULT_CLAM_URI = URI.create("http://clamav.nais.svc.nais.local/scan");
    boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public URI getPingURI() {
        return null;
    }

    public URI getUri() {
        return DEFAULT_CLAM_URI;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [enabled=" + enabled + ",uri=" + getUri() + "]";
    }
}
