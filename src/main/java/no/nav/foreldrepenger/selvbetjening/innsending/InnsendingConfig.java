package no.nav.foreldrepenger.selvbetjening.innsending;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;

@ConfigurationProperties(prefix = "mottak")
public class InnsendingConfig extends AbstractConfig {
    private static final String PING = "mottak/ping";
    private static final String SEND = "mottak/send";
    private static final String ENDRE = "mottak/endre";
    private static final String ETTERSEND = "mottak/ettersend";

    @ConstructorBinding
    public InnsendingConfig(URI uri, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
    }

    @Override
    protected URI pingURI() {
        return uri(getBaseUri(), PING);
    }

    URI innsendingURI() {
        return uri(getBaseUri(), SEND);
    }

    URI endringURI() {
        return uri(getBaseUri(), ENDRE);
    }

    URI ettersendingURI() {
        return uri(getBaseUri(), ETTERSEND);
    }

}
