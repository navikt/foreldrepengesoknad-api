package no.nav.foreldrepenger.selvbetjening.oppslag;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

@ConfigurationProperties("oppslag2")
public class OppslagOversiktConfig {

    private static final String PERSON = "api/person/info";
    private static final String ARBEIDSFORHOLD = "api/arbeid/mineArbeidsforhold";
    private final URI baseUri;

    public OppslagOversiktConfig(URI uri) {
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
