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
    private static final String BASE_PATH = "api";
    private static final String PING = "oppslag/ping";
    private static final String PERSON = "person";
    private static final String SØKERINFO = "søkerinfo";
    private static final String AKTØRFNR = "oppslag/aktorfnr";

    boolean enabled;
    URI oppslagURI;

    public OppslagConfig(@Value("${FPSOKNAD_OPPSLAG_API_URL}") URI oppslagURI) {
        this.oppslagURI = uri(oppslagURI, BASE_PATH);
    }

    public URI getOppslagURI() {
        return oppslagURI;
    }

    public void setOppslagURI(URI oppslagURI) {
        this.oppslagURI = oppslagURI;
    }

    URI getPingURI() {
        return uri(getOppslagURI(), PING);
    }

    URI getPersonURI() {
        return uri(getOppslagURI(), PERSON);
    }

    URI getSøkerinfoURI() {
        return uri(getOppslagURI(), SØKERINFO);
    }

    URI getAktørIdURI(String fnr) {
        return uri(getOppslagURI(), AKTØRFNR, queryParams(FNR, fnr));
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
