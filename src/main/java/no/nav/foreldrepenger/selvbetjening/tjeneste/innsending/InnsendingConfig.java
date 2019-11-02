package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.interceptors.client.ZoneCrossingAware;
import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractConfig;

@ConfigurationProperties(prefix = "mottak")
@ConstructorBinding
public class InnsendingConfig extends AbstractConfig implements ZoneCrossingAware {

    private static final String ENDRE = "/mottak/endre";

    private static final String ETTERSEND = "/mottak/ettersend";

    private static final String SEND = "/mottak/send";

    private static final String PING = "mottak/ping";

    private final String key;

    public InnsendingConfig(URI uri, String key, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
        this.key = key;
    }

    @Override
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
