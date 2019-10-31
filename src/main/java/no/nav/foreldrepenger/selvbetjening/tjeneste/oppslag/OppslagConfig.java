package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag;

import static no.nav.foreldrepenger.selvbetjening.tjeneste.UriUtil.queryParams;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.UriUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.tjeneste.ZoneCrossingAware;

@ConfigurationProperties("oppslag")
@Component
//@ConstructorBinding
public class OppslagConfig /* extends AbstractConfig */ implements ZoneCrossingAware {

    private static final String FNR = "fnr";
    private static final String PING = "oppslag/ping";
    private static final String PERSON = "person";
    private static final String SØKERINFO = "oppslag";
    private static final String AKTØRFNR = "oppslag/aktorfnr";

    private /* final */ String key;
    private /* final */ URI uri;
    private /* final */ boolean enabled;

    /*
     * public OppslagConfig(URI uri, String key, @DefaultValue("true") boolean
     * enabled) { super(uri, enabled); this.key = key; }
     */

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public URI zoneCrossingUri() {
        return getUri();
    }

    public URI pingURI() {
        return uri(getUri(), PING);
    }

    URI personURI() {
        return uri(getUri(), PERSON);
    }

    URI søkerInfoURI() {
        return uri(getUri(), SØKERINFO);
    }

    URI aktørIdURI(String fnr) {
        return uri(getUri(), AKTØRFNR, queryParams(FNR, fnr));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingURI=" + pingURI() + ", personURI=" + personURI()
                + ", søkerinfoURI=" + søkerInfoURI() + ", aktørIdURI=" + aktørIdURI("42") + "]";
    }

}
