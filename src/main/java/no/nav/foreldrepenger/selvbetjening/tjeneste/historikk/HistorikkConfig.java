package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.queryParams;
import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "historikk", ignoreUnknownFields = false)
@Component
public class HistorikkConfig {

    private boolean enabled = true;
    private URI uri;
    private String key;

    public void setKey(String key) {
        this.key = key;
    }

    private static final String DEFAULT_PING_PATH = "actuator/info";

    public URI historikkURI() {
        return uri(uri, "historikk" + "/me/all");
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public URI historikkPreprodURI(String fnr) {
        return uri(uri, "historikk" + "/" + DEV + "/all", queryParams("fnr", fnr));
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
