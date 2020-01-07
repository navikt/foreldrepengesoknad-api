package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.interceptors.client.ZoneCrossingAware;
import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractConfig;
import no.nav.foreldrepenger.selvbetjening.util.StringUtil;

@ConfigurationProperties("oppslag")
public class OppslagConfig extends AbstractConfig implements ZoneCrossingAware {

    private static final String FNR = "fnr";
    private static final String PING = "oppslag/ping";
    private static final String PERSON = "person";
    private static final String SØKERINFO = "oppslag";
    private static final String AKTØRFNR = "oppslag/aktorfnr";

    private final String key;

    public OppslagConfig(URI uri, String key, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
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
        return getClass().getSimpleName() + "[key=" + StringUtil.limit(key, 3) + ", zoneCrossingUri()="
                + zoneCrossingUri() + "]";
    }

}
