package no.nav.foreldrepenger.selvbetjening.innsyn;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;

@Deprecated
@ConfigurationProperties(prefix = "innsyn")
public class InnsynConfig extends AbstractConfig {

    private static final String PING = "actuator/health/liveness";
    private static final String ARBEIDSFORHOLD = "innsyn/arbeidsforhold";

    public InnsynConfig(URI uri, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
    }

    @Override
    public URI pingURI() {
        return uri(getBaseUri(), PING);
    }

    public URI arbeidsforholdURI() {
        return uri(getBaseUri(), ARBEIDSFORHOLD);
    }
}
