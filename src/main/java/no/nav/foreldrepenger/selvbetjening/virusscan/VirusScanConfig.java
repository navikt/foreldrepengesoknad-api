package no.nav.foreldrepenger.selvbetjening.virusscan;

import java.net.URI;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import lombok.SneakyThrows;
import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;

@ConfigurationProperties(prefix = "virus")
public class VirusScanConfig extends AbstractConfig {

    private static final String DEFAULT_CLAM_URI = "http://clamav.nais.svc.nais./scan";

    @ConstructorBinding
    public VirusScanConfig(@DefaultValue(DEFAULT_CLAM_URI) URI uri, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
    }

    @Override
    @SneakyThrows
    protected URI pingURI() {
        return new URIBuilder()
                .setScheme(getUri().getScheme())
                .setHost(getUri().getHost())
                .build();
    }
}
