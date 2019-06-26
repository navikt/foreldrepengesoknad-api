package no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "minidialog")
@Configuration
public class MinidialogConfig {

    private static final String MINIDIALOG = "minidialog/me";
    private static final String SVAR = "minidialog/svar";

    public URI getURI() {
        return uri;
    }

    private static final String DEFAULT_PING_PATH = "actuator/info";
    private boolean enabled;
    private final URI uri;

    public MinidialogConfig(@Value("${FPSOKNAD_HISTORIKK_API_URL}") URI uri) {
        this.uri = uri;
    }

    public URI minidialogURI() {
        return uri(getURI(), MINIDIALOG);
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

    public URI svarURI() {
        return uri(getURI(), SVAR);
    }
}
