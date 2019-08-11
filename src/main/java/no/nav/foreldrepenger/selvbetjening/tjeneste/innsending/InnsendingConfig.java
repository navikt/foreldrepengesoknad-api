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

    private boolean enabled = true;
    private final URI uri;
    private final String apikey;

    public InnsendingConfig(@Value("${FPSOKNAD_MOTTAK_API_URL}") URI uri, @Value("${mottak.apikey}") String key) {
        this.uri = uri;
        this.apikey = key;
    }

    URI getPingURI() {
        return uri(uri, PING);
    }

    URI getInnsendingURI() {
        return uri(uri, SEND);
    }

    URI getEndringURI() {
        return uri(uri, ENDRE);
    }

    URI getEttersendingURI() {
        return uri(uri, ETTERSEND);
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
