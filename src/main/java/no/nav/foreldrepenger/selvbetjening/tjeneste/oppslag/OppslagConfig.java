package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

@ConfigurationProperties(prefix = "oppslag")
@Configuration
public class OppslagConfig {

    private static final String BASE_PATH = "api";
    private static final String PING = "oppslag/ping";
    static final String PERSON = "person";
    static final String SØKERINFO = "søkerinfo";
    static final String AKTØRFNR = "oppslag/aktorfnr";

    boolean enabled;
    URI oppslagURI;
    String basePath;

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
        return uri(getOppslagURI(), AKTØRFNR, queryParams("fnr", fnr));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    protected static URI uri(URI base, String path) {
        return uri(base, path, null);
    }

    protected static URI uri(URI base, String path, HttpHeaders queryParams) {
        return builder(base, path, queryParams)
                .build()
                .toUri();
    }

    private static UriComponentsBuilder builder(URI base, String path, HttpHeaders queryParams) {
        return UriComponentsBuilder
                .fromUri(base)
                .pathSegment(path)
                .queryParams(queryParams);

    }

    protected static HttpHeaders queryParams(String key, String value) {
        HttpHeaders queryParams = new HttpHeaders();
        queryParams.add(key, value);
        return queryParams;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingURI=" + getPingURI() + ", personURI=" + getPersonURI()
                + ", søkerinfoURI=" + getSøkerinfoURI() + ", aktørIdURI=" + getAktørIdURI("42") + "]";
    }
}
