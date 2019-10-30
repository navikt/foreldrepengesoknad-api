package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractConfig;

@ConfigurationProperties("oppslag")
@ConstructorBinding
public class OppslagConfig extends AbstractConfig {

    private static final String FNR = "fnr";
    private static final String PING = "oppslag/ping";
    private static final String PERSON = "person";
    private static final String SØKERINFO = "oppslag";
    private static final String AKTØRFNR = "oppslag/aktorfnr";

    private final boolean enabled;
    private final URI uri;
    private final String key;

    public OppslagConfig(URI uri, String key, @DefaultValue("true") boolean enabled) {
        this.enabled = enabled;
        this.uri = uri;
        this.key = key;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public URI getUri() {
        return uri;
    }

    public String getKey() {
        return key;
    }

    public URI pingURI() {
        return uri(uri, PING);
    }

    URI personURI() {
        return uri(uri, PERSON);
    }

    URI søkerInfoURI() {
        return uri(uri, SØKERINFO);
    }

    URI aktørIdURI(String fnr) {
        return uri(uri, AKTØRFNR, queryParams(FNR, fnr));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingURI=" + pingURI() + ", personURI=" + personURI()
                + ", søkerinfoURI=" + søkerInfoURI() + ", aktørIdURI=" + aktørIdURI("42") + "]";
    }

}
