package no.nav.foreldrepenger.selvbetjening.oppslag;

import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.net.URI;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

@ConfigurationProperties("oppslag")
public class OppslagConfig extends AbstractConfig {

    private static final String PING = "actuator/health/liveness";
    private static final String PERSON = "oppslag/person";

    public OppslagConfig(URI uri, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
    }

    @Override
    public URI pingURI() {
        return uri(getBaseUri(), PING);
    }

    URI personURI() {
        return uri(getBaseUri(), PERSON);
    }

}
