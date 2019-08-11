package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtils.uri;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import no.nav.foreldrepenger.selvbetjening.config.AbstractConfig;

@ConfigurationProperties(prefix = "innsending")
@Configuration
public class InnsendingConfig extends AbstractConfig {
    private static final String ENDRE = "/mottak/endre";

    private static final String ETTERSEND = "/mottak/ettersend";

    private static final String SEND = "/mottak/send";

    private static final String PING = "mottak/ping";

    public InnsendingConfig(@Value("${FPSOKNAD_MOTTAK_API_URL}") URI uri) {
        super(uri);
    }

    URI getPingURI() {
        return uri(getUri(), PING);
    }

    URI getInnsendingURI() {
        return uri(getUri(), SEND);
    }

    URI getEndringURI() {
        return uri(getUri(), ENDRE);
    }

    URI getEttersendingURI() {
        return uri(getUri(), ETTERSEND);
    }

}
