package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtils.queryParams;
import static no.nav.foreldrepenger.selvbetjening.util.URIUtils.uri;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "oppslag")
@Configuration
public class OppslagConfig {

    private static final String FNR = "fnr";
    private static final String PING = "oppslag/ping";
    private static final String PERSON = "person";
    private static final String SØKERINFO = "oppslag";
    private static final String AKTØRFNR = "oppslag/aktorfnr";

    private boolean enabled;
    private final URI uri;

    public OppslagConfig(@Value("${FPSOKNAD_OPPSLAG_API_URL}") URI uri) {
        this.uri = uri;
    }

    private URI getURI() {
        return uri;
    }

    URI getPingURI() {
        return uri(getURI(), PING);
    }

    URI getPersonURI() {
        return uri(getURI(), PERSON);
    }

    URI getSøkerinfoURI() {
        return uri(getURI(), SØKERINFO);
    }

    URI getAktørIdURI(String fnr) {
        return uri(getURI(), AKTØRFNR, queryParams(FNR, fnr));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingURI=" + getPingURI() + ", personURI=" + getPersonURI()
                + ", søkerinfoURI=" + getSøkerinfoURI() + ", aktørIdURI=" + getAktørIdURI("42") + "]";
    }
}
