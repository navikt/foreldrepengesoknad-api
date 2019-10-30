package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractConfig;

@ConfigurationProperties(prefix = "oppslag")
@Component
public class OppslagConfig extends AbstractConfig {

    private static final String FNR = "fnr";
    private static final String PING = "oppslag/ping";
    private static final String PERSON = "person";
    private static final String SØKERINFO = "oppslag";
    private static final String AKTØRFNR = "oppslag/aktorfnr";

    private boolean enabled = true;
    private URI uri;
    private String key;

    public boolean isEnabled() {
        return enabled;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public URI getUri() {
        return uri;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public URI pingURI() {
        return uri(uri, PING);
    }

    URI getPersonURI() {
        return uri(uri, PERSON);
    }

    URI getSøkerinfoURI() {
        return uri(uri, SØKERINFO);
    }

    URI getAktørIdURI(String fnr) {
        return uri(uri, AKTØRFNR, queryParams(FNR, fnr));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingURI=" + pingURI() + ", personURI=" + getPersonURI()
                + ", søkerinfoURI=" + getSøkerinfoURI() + ", aktørIdURI=" + getAktørIdURI("42") + "]";
    }

}
