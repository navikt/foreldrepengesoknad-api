package no.nav.foreldrepenger.selvbetjening.historikk;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;
import no.nav.foreldrepenger.selvbetjening.util.URIUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.net.URI;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

@ConfigurationProperties("historikk")
public class HistorikkConfig extends AbstractConfig {
    private static final String DEFAULT_PING_PATH = "actuator/health/liveness";

    private static final String HISTORIKK = "historikk";
    private static final String HISTORIKK_ALL_PATH = HISTORIKK + "/me/all";
    private static final String MANGLEDEVEDLEGG_PATH = HISTORIKK + "/me/manglendevedlegg";

    private static final String SAKSNUMMER = "saksnummer";

    public HistorikkConfig(URI uri, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
    }

    public URI historikkURI() {
        return uri(getBaseUri(), HISTORIKK_ALL_PATH);
    }

    public URI vedleggURI(Saksnummer saksnr) {
        return uri(getBaseUri(), MANGLEDEVEDLEGG_PATH, URIUtil.queryParam(SAKSNUMMER, saksnr.value()));
    }

    @Override
    public URI pingURI() {
        return uri(getBaseUri(), DEFAULT_PING_PATH);
    }

}
