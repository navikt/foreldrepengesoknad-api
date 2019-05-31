package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "historikk")
@Configuration
public class HistorikkConfig {

    private static final String HISTORIKK = "historikk";
    private static final URI DEFAULT_BASE_URI = URI.create("http://fpinfo-historikk/api");
    private static final String DEFAULT_PING_PATH = "actuator/info";
    private boolean enabled;

    public URI historikkURI() {
        return uri(DEFAULT_BASE_URI, HISTORIKK);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public URI pingURI() {
        return uri(DEFAULT_BASE_URI, DEFAULT_PING_PATH);
    }
}
