package no.nav.foreldrepenger.selvbetjening.innsending;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;

@ConfigurationProperties(prefix = "mottak")
public class InnsendingConfig extends AbstractConfig {

    private static final String ENDRE = "/mottak/endre";

    private static final String ETTERSEND = "/mottak/ettersend";

    private static final String SEND = "/mottak/send";

    private static final String PING = "mottak/ping";

    @ConstructorBinding
    public InnsendingConfig(URI uri, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
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

}
