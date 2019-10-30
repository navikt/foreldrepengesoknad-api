package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractConfig;

@ConfigurationProperties(prefix = "mottak")
@ConstructorBinding
public class InnsendingConfig extends AbstractConfig {
    private static final String ENDRE = "/mottak/endre";

    private static final String ETTERSEND = "/mottak/ettersend";

    private static final String SEND = "/mottak/send";

    private static final String PING = "mottak/ping";

    private final boolean enabled;
    private final URI uri;
    private final String key;

    public InnsendingConfig(URI uri, String key, @DefaultValue("true") boolean enabled) {
        this.uri = uri;
        this.key = key;
        this.enabled = enabled;
    }

    @Override
    protected URI pingURI() {
        return uri(uri, PING);

    }

    URI innsendingURI() {
        return uri(uri, SEND);
    }

    URI endringURI() {
        return uri(uri, ENDRE);
    }

    URI ettersendingURI() {
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
