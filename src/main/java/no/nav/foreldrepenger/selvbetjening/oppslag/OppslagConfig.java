package no.nav.foreldrepenger.selvbetjening.oppslag;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("oppslag")
public class OppslagConfig {

    private static final String PERSON = "oppslag/person";
    private static final String ARBEIDSFORHOLD = "oppslag/person/arbeidsforhold";
    private final URI baseUri;

    public OppslagConfig(URI uri) {
        this.baseUri = uri;
    }

    URI personURI() {
        return uri(getBaseUri(), PERSON);
    }

    URI arbeidsforholdURI() {
        return uri(getBaseUri(), ARBEIDSFORHOLD);
    }

    private URI getBaseUri() {
        return baseUri;
    }
}
