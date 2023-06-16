package no.nav.foreldrepenger.selvbetjening.innsyn;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;

@ConfigurationProperties(prefix = "oversikt")
public class InnsynConfig extends AbstractConfig {

    private static final String PING = "internal/health/isAlive";
    private static final String SAKER = "api/saker";
    private static final String ANNENPART_VEDTAK = "api/annenPart";

    protected InnsynConfig(URI uri, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
    }

    @Override
    protected URI pingURI() {
        return uri(getBaseUri(), PING);
    }

    URI saker() {
        return uri(getBaseUri(), SAKER);
    }

    URI annenpartsVedtak() {
        return uri(getBaseUri(), ANNENPART_VEDTAK);
    }
}
