package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtils.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "mottak")
@Component
public class InnsendingConfig {
    private static final String ENDRE = "/mottak/endre";

    private static final String ETTERSEND = "/mottak/ettersend";

    private static final String SEND = "/mottak/send";

    private static final String PING = "mottak/ping";

    private boolean enabled = true;
    private URI uri;
    private String key;

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getKey() {
        return key;
    }

    public URI getUri() {
        return uri;
    }

}
