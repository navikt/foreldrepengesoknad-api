package no.nav.foreldrepenger.selvbetjening.innsyn;

import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.net.URI;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

@ConfigurationProperties(prefix = "oversikt")
public class OversiktConfig extends AbstractConfig {

    private static final String SAKER = "/api/saker";

    @ConstructorBinding
    protected OversiktConfig(URI baseUri, boolean enabled) {
        super(baseUri, enabled);
    }

    @Override
    protected URI pingURI() {
        return null;
    }

    URI saker() {
        return uri(getBaseUri(), SAKER);
    }
}
