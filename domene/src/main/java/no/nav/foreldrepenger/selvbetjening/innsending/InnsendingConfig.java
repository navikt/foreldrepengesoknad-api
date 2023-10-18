package no.nav.foreldrepenger.selvbetjening.innsending;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mottak")
public class InnsendingConfig {
    private static final String SEND = "mottak/send";
    private static final String ENDRE = "mottak/endre";
    private static final String ETTERSEND = "mottak/ettersend";

    private final URI baseUri;

    public InnsendingConfig(URI uri) {
        this.baseUri = uri;
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

    private URI getBaseUri() {
        return baseUri;
    }
}
