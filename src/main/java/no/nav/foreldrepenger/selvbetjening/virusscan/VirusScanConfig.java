package no.nav.foreldrepenger.selvbetjening.virusscan;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;

@ConfigurationProperties(prefix = "virus")
public class VirusScanConfig extends AbstractConfig {

    private static final String DEFAULT_CLAM_URI = "http://clamav.nais.svc.nais./scan";

    @ConstructorBinding
    public VirusScanConfig(@DefaultValue(DEFAULT_CLAM_URI) URI uri, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
    }

    @Override
    protected URI pingURI() {
        return getUri();
    }
}
