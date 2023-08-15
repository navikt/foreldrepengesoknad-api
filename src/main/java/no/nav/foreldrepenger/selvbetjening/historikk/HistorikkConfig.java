package no.nav.foreldrepenger.selvbetjening.historikk;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.util.URIUtil;

@ConfigurationProperties("historikk")
public class HistorikkConfig {

    private static final String HISTORIKK = "historikk";
    private static final String HISTORIKK_ALL_PATH = HISTORIKK + "/me/all";
    private static final String MANGLEDEVEDLEGG_PATH = HISTORIKK + "/me/manglendevedlegg";

    private static final String SAKSNUMMER = "saksnummer";
    private final URI baseUri;

    public HistorikkConfig(URI uri) {
        this.baseUri = uri;
    }

    public URI historikkURI() {
        return uri(getBaseUri(), HISTORIKK_ALL_PATH);
    }

    public URI vedleggURI(Saksnummer saksnr) {
        return uri(getBaseUri(), MANGLEDEVEDLEGG_PATH, URIUtil.queryParam(SAKSNUMMER, saksnr.value()));
    }

    private URI getBaseUri() {
        return baseUri;
    }
}
