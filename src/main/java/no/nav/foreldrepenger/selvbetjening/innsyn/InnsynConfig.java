package no.nav.foreldrepenger.selvbetjening.innsyn;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oversikt")
public class InnsynConfig {

    private static final String SAKER = "api/saker";
    private static final String ANNENPART_VEDTAK = "api/annenPart";
    private final URI baseUri;

    protected InnsynConfig(URI uri) {
        this.baseUri = uri;
    }

    URI saker() {
        return uri(getBaseUri(), SAKER);
    }

    URI annenpartsVedtak() {
        return uri(getBaseUri(), ANNENPART_VEDTAK);
    }

    private URI getBaseUri() {
        return baseUri;
    }
}
