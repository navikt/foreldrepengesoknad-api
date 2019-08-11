package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtils.uri;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "innsending")
@Configuration
public class InnsendingConfig {
    private static final String ENDRE = "/mottak/endre";

    private static final String ETTERSEND = "/mottak/ettersend";

    private static final String SEND = "/mottak/send";

    private static final String PING = "mottak/ping";

    private boolean enabled;
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    private final URI uri;

    public InnsendingConfig(@Value("${FPSOKNAD_MOTTAK_API_URL}") URI uri) {
        this.uri = uri;
    }

    public URI getURI() {
        return uri;
    }

    URI getPingURI() {
        return uri(getURI(), PING);
    }

    URI getInnsendingURI() {
        return uri(getURI(), SEND);
    }

    URI getEndringURI() {
        return uri(getURI(), ENDRE);
    }

    URI getEttersendingURI() {
        return uri(getURI(), ETTERSEND);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
