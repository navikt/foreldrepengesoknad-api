package no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "virus")
@Configuration
public class VirusScanConfig {

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
        return URI.create("http://clamav.nais/scan");
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [enabled=" + enabled + ",uri=" + getUri() + "]";
    }
}
