package no.nav.foreldrepenger.selvbetjening.vedlegg.virusscan;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "virus")
public class VirusScanConfig {

    private static final String DEFAULT_CLAM_URI = "http://clamav.nais.svc.nais.local/scan";

    private final URI baseUri;
    private final boolean enabled;

    public VirusScanConfig(@DefaultValue(DEFAULT_CLAM_URI) URI uri, @DefaultValue("true") boolean enabled) {
        this.baseUri = uri;
        this.enabled = enabled;
    }

    public URI getBaseUri() {
        return baseUri;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
