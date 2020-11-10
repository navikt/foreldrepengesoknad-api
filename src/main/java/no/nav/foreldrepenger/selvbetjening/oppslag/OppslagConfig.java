package no.nav.foreldrepenger.selvbetjening.oppslag;

import static no.nav.foreldrepenger.selvbetjening.util.StringUtil.limit;
import static org.apache.commons.lang3.StringUtils.reverse;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;
import no.nav.foreldrepenger.selvbetjening.http.interceptors.ZoneCrossingAware;

@ConfigurationProperties("oppslag")
public class OppslagConfig extends AbstractConfig implements ZoneCrossingAware {

    private static final String FNR = "fnr";
    private static final String PING = "actuator/health/liveness";
    private static final String PERSON = "oppslag/person";
    private static final String SØKERINFO = "oppslag";
    private static final String AKTØRFNR = "oppslag/aktoer";

    private final String key;
    private final URI pdlUri;

    @ConstructorBinding
    public OppslagConfig(URI uri, URI pdlUri, String key, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
        this.key = key;
        this.pdlUri = pdlUri;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public URI zoneCrossingUri() {
        return getUri();
        // return pdlUri;

    }

    @Override
    public URI pingURI() {
        return uri(pdlUri, PING);
    }

    URI personURI() {
        return uri(pdlUri, PERSON);
    }

    URI søkerInfoURI() {
        return uri(getUri(), SØKERINFO);
    }

    URI aktørIdURI(String fnr) {
        return uri(pdlUri, AKTØRFNR, queryParams(FNR, fnr));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[key=" + limit(reverse(key), 3)
                + ", zoneCrossingUri=" + zoneCrossingUri() + "]";
    }

}
