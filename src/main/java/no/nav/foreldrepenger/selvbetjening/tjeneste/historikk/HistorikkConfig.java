package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.queryParams;
import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "historikk", ignoreUnknownFields = false)
@Configuration
public class HistorikkConfig {

    private static final String HISTORIKK = "historikk";
    private static final String HISTORIKK_PREPROD = HISTORIKK + "/preprod";
    private boolean enabled = true;
    private final URI uri;
    private final String apikey;
    private static final String DEFAULT_PING_PATH = "actuator/info";

    public HistorikkConfig(@Value("${FPSOKNAD_HISTORIKK_API_URL}") URI uri, @Value("${apikey.historikk}") String key) {
        this.uri = uri;
        this.apikey = key;
    }

    public URI historikkURI() {
        return uri(uri, HISTORIKK + "/me");
    }

    public URI historikkPreprodURI(String fnr) {
        return uri(uri, HISTORIKK_PREPROD + "/hent", queryParams("fnr", fnr));
    }

    public URI pingURI() {
        return uri(uri, DEFAULT_PING_PATH);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getApikey() {
        return apikey;
    }

    public URI getUri() {
        return uri;
    }
}
