package no.nav.foreldrepenger.selvbetjening.oppslag;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

@ConfigurationProperties("oppslag")
public class OppslagConfig {

    private static final String PERSON = "api/person/info";
    private static final String PERSON_MED_ARBEIDSFORHOLD = "api/person/info-med-arbeidsforhold";
    private final URI baseUri;

    public OppslagConfig(URI uri) {
        this.baseUri = uri;
    }

    URI personURI() {
        return uri(getBaseUri(), PERSON);
    }

    URI personMedArbeidsforhold() {
        return uri(getBaseUri(), PERSON_MED_ARBEIDSFORHOLD);
    }

    private URI getBaseUri() {
        return baseUri;
    }

}
