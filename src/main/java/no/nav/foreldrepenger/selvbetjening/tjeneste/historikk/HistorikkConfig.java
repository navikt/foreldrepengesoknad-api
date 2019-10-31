package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import static no.nav.foreldrepenger.selvbetjening.tjeneste.UriUtil.queryParams;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.UriUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.tjeneste.ZoneCrossingAware;

@ConfigurationProperties("historikk")
//@ConstructorBinding
@Component
public class HistorikkConfig /* extends AbstractConfig */ implements ZoneCrossingAware {
    private static final String DEFAULT_PING_PATH = "actuator/info";

    private /* final */ String key;
    private /* final */ URI uri;
    private /* final */ boolean enabled;

    /*
     * public HistorikkConfig(URI uri, String key, @DefaultValue("true") boolean
     * enabled) { super(uri, enabled); this.key = key; }
     */

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public URI historikkURI() {
        return uri(getUri(), "historikk" + "/me/all");
    }

    public URI historikkPreprodURI(String fnr) {
        return uri(getUri(), "historikk" + "/dev/all", queryParams("fnr", fnr));
    }

    public URI pingURI() {
        return uri(getUri(), DEFAULT_PING_PATH);
    }

    @Override
    public String getKey() {
        return key;
    }

    public URI minidialogURI() {
        return uri(getUri(), "/me/minidialoger/spm");

    }

    @Override
    public URI zoneCrossingUri() {
        return getUri();
    }
}
