package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractConfig;

@ConfigurationProperties("historikk")
@ConstructorBinding
public class HistorikkConfig extends AbstractConfig {
    private static final String DEFAULT_PING_PATH = "actuator/info";

    private final boolean enabled;
    private final URI uri;
    private final String key;

    public HistorikkConfig(URI uri, String key, @DefaultValue("true") boolean enabled) {
        this.uri = uri;
        this.key = key;
        this.enabled = enabled;
    }

    public URI historikkURI() {
        return uri(uri, "historikk" + "/me/all");
    }
 
    public URI historikkPreprodURI(String fnr) {
        return uri(uri, "historikk" + "/dev/all", queryParams("fnr", fnr));
    }

    public URI pingURI() {
        return uri(uri, DEFAULT_PING_PATH);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getKey() {
        return key;
    }

    public URI getUri() {
        return uri;
    }

    public URI minidialogURI() {
        return uri(uri, "/me/minidialoger/spm");

    }
}
