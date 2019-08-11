package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtils.queryParams;
import static no.nav.foreldrepenger.selvbetjening.util.URIUtils.uri;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "oppslag", ignoreUnknownFields = false)
@Configuration
public class OppslagConfig {

    private static final String FNR = "fnr";
    private static final String PING = "oppslag/ping";
    private static final String PERSON = "person";
    private static final String SØKERINFO = "oppslag";
    private static final String AKTØRFNR = "oppslag/aktorfnr";

    private boolean enabled = true;
    private final URI uri;
    private final String apikey;

    public OppslagConfig(@Value("${FPSOKNAD_OPPSLAG_API_URL}") URI uri, @Value("${oppslag.apikey}") String key) {
        this.uri = uri;
        this.apikey = key;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getApikey() {
        return apikey;
    }

    URI getPingURI() {
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
        return getClass().getSimpleName() + " [pingURI=" + getPingURI() + ", personURI=" + getPersonURI()
                + ", søkerinfoURI=" + getSøkerinfoURI() + ", aktørIdURI=" + getAktørIdURI("42") + "]";
    }

    public URI getUri() {
        return uri;
    }
}
