package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.queryParams;
import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import no.nav.foreldrepenger.selvbetjening.config.AbstractConfig;

@ConfigurationProperties(prefix = "historikk")
@Configuration
public class HistorikkConfig extends AbstractConfig {

    private static final String HISTORIKK = "historikk";
    private static final String HISTORIKK_PREPROD = HISTORIKK + "/preprod";

    private static final String DEFAULT_PING_PATH = "actuator/info";

    public HistorikkConfig(@Value("${FPSOKNAD_HISTORIKK_API_URL}") URI uri, @Value("${apikey.historikk}") String key) {
        super(uri, key);
    }

    public URI historikkURI() {
        return uri(getUri(), HISTORIKK + "/me");
    }

    public URI historikkPreprodURI(String fnr) {
        return uri(getUri(), HISTORIKK_PREPROD + "/hent", queryParams("fnr", fnr));
    }

    public URI pingURI() {
        return uri(getUri(), DEFAULT_PING_PATH);
    }
}
