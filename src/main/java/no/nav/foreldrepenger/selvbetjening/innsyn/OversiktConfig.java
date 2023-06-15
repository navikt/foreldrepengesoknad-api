package no.nav.foreldrepenger.selvbetjening.innsyn;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;

@ConfigurationProperties(prefix = "oversikt")
public class OversiktConfig extends AbstractConfig {

    private static final String SAKER = "api/saker";
    private static final String ARBEIDSFORHOLD = "innsyn/arbeidsforhold";
    private static final String ANNENPART_VEDTAK = "api/annenPart";

    protected OversiktConfig(URI uri, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
    }

    @Override
    protected URI pingURI() {
        return null;
    }

    URI saker() {
        return uri(getBaseUri(), SAKER);
    }

    URI annenpartsVedtak() {
        return uri(getBaseUri(), ANNENPART_VEDTAK);
    }

    URI arbeidsforholdURI() {
        return uri(getBaseUri(), ARBEIDSFORHOLD);
    }
}
