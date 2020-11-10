package no.nav.foreldrepenger.selvbetjening.oppslag;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;

@ConfigurationProperties("oppslag")
public class OppslagConfig extends AbstractConfig {

    private static final String PING = "actuator/health/liveness";
    private static final String PERSON = "oppslag/person";

    @ConstructorBinding
    public OppslagConfig(URI uri, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
    }

    @Override
    public URI pingURI() {
        return uri(getUri(), PING);
    }

    URI personURI() {
        return uri(getUri(), PERSON);
    }

}
