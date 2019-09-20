package no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog;

import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.queryParams;
import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = MinidialogConfig.MINIDIALOG)
@Configuration
public class MinidialogConfig {

    static final String MINIDIALOG = "minidialog";
    private static final String MINIDIALOG_ME = MINIDIALOG + "/me";
    private static final String MINIDIALOG_DEV = MINIDIALOG + "/" + DEV;
    private static final String AKTIVE = MINIDIALOG_DEV + "/spm";

    private static final String DEFAULT_PING_PATH = "actuator/info";
    private boolean enabled;
    private final URI uri;

    public MinidialogConfig(@Value("${historikk.uri}") URI uri) {
        this.uri = uri;
    }

    public URI getURI() {
        return uri;
    }

    public URI minidialogURI() {
        return uri(getURI(), MINIDIALOG_ME);
    }

    public URI minidialogPreprodURI(String fnr) {
        return uri(getURI(), AKTIVE, queryParams("fnr", fnr));
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
