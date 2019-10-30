package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractConfig;

@ConfigurationProperties(prefix = "historikk", ignoreUnknownFields = false)
@Component
public class HistorikkConfig extends AbstractConfig {
    private static final String DEFAULT_PING_PATH = "actuator/info";

    private boolean enabled = true;
    private URI uri;
    private String key;

    public void setKey(String key) {
        this.key = key;
    }

    public URI historikkURI() {
        return uri(uri, "historikk" + "/me/all");
    }

    public void setUri(URI uri) {
        this.uri = uri;
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
