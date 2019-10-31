package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import static no.nav.foreldrepenger.selvbetjening.tjeneste.UriUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.tjeneste.ZoneCrossingAware;

@ConfigurationProperties(prefix = "mottak")
//@ConstructorBinding
@Component
public class InnsendingConfig /* extends AbstractConfig */ implements ZoneCrossingAware {
    public void setKey(String key) {
        this.key = key;
    }

    private static final String ENDRE = "/mottak/endre";

    private static final String ETTERSEND = "/mottak/ettersend";

    private static final String SEND = "/mottak/send";

    private static final String PING = "mottak/ping";

    private /* final */ String key;
    private /* final */ URI uri;
    private /* final */ boolean enabled;

    /*
     * public InnsendingConfig(URI uri, String key, @DefaultValue("true") boolean
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

    // @Override
    protected URI pingURI() {
        return uri(getUri(), PING);
    }

    URI innsendingURI() {
        return uri(getUri(), SEND);
    }

    URI endringURI() {
        return uri(getUri(), ENDRE);
    }

    URI ettersendingURI() {
        return uri(getUri(), ETTERSEND);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public URI zoneCrossingUri() {
        return getUri();
    }
}
