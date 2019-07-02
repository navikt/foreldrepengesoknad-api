package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.queryParams;
import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "historikk")
@Configuration
public class HistorikkConfig {

    private static final String HISTORIKK = "historikk";
    private static final String HISTORIKK_PREPROD = HISTORIKK + "/preprod";

    public URI getURI() {
        return uri;
    }

    private static final String DEFAULT_PING_PATH = "actuator/info";
    private boolean enabled;
    private final URI uri;

    public HistorikkConfig(@Value("${FPSOKNAD_HISTORIKK_API_URL}") URI uri) {
        this.uri = uri;
    }

    public URI historikkURI() {
        return uri(getURI(), HISTORIKK + "/me");
    }

    public URI historikkPreprodURI(String fnr) {
        return uri(getURI(), HISTORIKK_PREPROD + "/hent", queryParams("fnr", fnr));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public URI pingURI() {
        return uri(getURI(), DEFAULT_PING_PATH);
    }
}
