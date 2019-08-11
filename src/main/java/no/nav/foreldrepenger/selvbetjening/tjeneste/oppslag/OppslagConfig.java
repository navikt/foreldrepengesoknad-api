package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtils.queryParams;
import static no.nav.foreldrepenger.selvbetjening.util.URIUtils.uri;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import no.nav.foreldrepenger.selvbetjening.config.AbstractConfig;

@ConfigurationProperties(prefix = "oppslag")
@Configuration
public class OppslagConfig extends AbstractConfig {

    private static final String FNR = "fnr";
    private static final String PING = "oppslag/ping";
    private static final String PERSON = "person";
    private static final String SØKERINFO = "oppslag";
    private static final String AKTØRFNR = "oppslag/aktorfnr";

    public OppslagConfig(@Value("${FPSOKNAD_OPPSLAG_API_URL}") URI uri, @Value("${oppslag.apikey}") String key) {
        super(uri, key);
    }

    URI getPingURI() {
        return uri(getUri(), PING);
    }

    URI getPersonURI() {
        return uri(getUri(), PERSON);
    }

    URI getSøkerinfoURI() {
        return uri(getUri(), SØKERINFO);
    }

    URI getAktørIdURI(String fnr) {
        return uri(getUri(), AKTØRFNR, queryParams(FNR, fnr));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingURI=" + getPingURI() + ", personURI=" + getPersonURI()
                + ", søkerinfoURI=" + getSøkerinfoURI() + ", aktørIdURI=" + getAktørIdURI("42") + "]";
    }
}
