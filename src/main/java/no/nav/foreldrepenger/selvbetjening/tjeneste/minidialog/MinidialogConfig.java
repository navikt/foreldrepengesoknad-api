package no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog;

import static no.nav.foreldrepenger.selvbetjening.tjeneste.UriUtil.queryParams;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.UriUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.util.Pair;

@ConfigurationProperties(MinidialogConfig.MINIDIALOG)
//@ConstructorBinding
@Component
public class MinidialogConfig /* extends AbstractConfig */ {

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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

    static final String MINIDIALOG = "minidialog";
    private static final String MINIDIALOG_DEV = MINIDIALOG + "/dev";
    private static final String MINIDIALOGER = MINIDIALOG_DEV + "/minidialoger";
    private static final String AKTIVE = MINIDIALOG_DEV + "/spm";

    private static final String DEFAULT_PING_PATH = "actuator/info";
    private /* final */ String key;
    private /* final */ URI uri;
    private /* final */ boolean enabled;
    /*
     * public MinidialogConfig(URI uri, @DefaultValue("true") boolean enabled) {
     * super(uri, enabled); }
     */

    public URI minidialogPreprodURI(String fnr, boolean activeOnly) {
        return uri(getUri(), MINIDIALOGER, queryParams(Pair.of("fnr", fnr), Pair.of("activeOnly", activeOnly)));
    }

    public URI aktiveSpmURI() {
        return uri(getUri(), MINIDIALOG + "/me");
    }

    public URI aktiveSpmURI(String fnr) {
        return uri(getUri(), AKTIVE, queryParams(Pair.of("fnr", fnr)));
    }

    public URI pingURI() {
        return uri(getUri(), DEFAULT_PING_PATH);
    }

}
