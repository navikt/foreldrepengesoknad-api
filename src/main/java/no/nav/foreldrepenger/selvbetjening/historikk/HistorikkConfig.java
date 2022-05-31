package no.nav.foreldrepenger.selvbetjening.historikk;

import static no.nav.foreldrepenger.common.util.Constants.FNR;
import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.queryParams;
import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;
import no.nav.foreldrepenger.selvbetjening.util.URIUtil;

@ConfigurationProperties("historikk")
public class HistorikkConfig extends AbstractConfig {
    private static final String DEFAULT_PING_PATH = "actuator/health/liveness";
    private static final String HISTORIKK = "/historikk";
    private static final String MANGLEDEVEDLEGG_PATH = HISTORIKK + "/me/manglendevedlegg";

    // Only DEV (TODO: Gjør dette på en bedre måte)
    private static final String HISTORIKK_DEV_PATH = HISTORIKK + "/dev/all";
    private static final String VEDLEGG_DEV_PATH = "/dev/vedlegg";

    private static final String SAKSNUMMER = "saksnummer";

    @ConstructorBinding
    public HistorikkConfig(URI uri, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
    }

    public URI historikkURI() {
        return uri(getBaseUri(), MANGLEDEVEDLEGG_PATH + "/me/all");
    }

    public URI vedleggURI(String saksnr) {
        return uri(getBaseUri(), MANGLEDEVEDLEGG_PATH, URIUtil.queryParam(SAKSNUMMER, saksnr));
    }

    public URI historikkPreprodURI(String fnr) {
        return uri(getBaseUri(), HISTORIKK_DEV_PATH, URIUtil.queryParam(FNR, fnr));
    }

    public URI vedleggPreprodURI(Fødselsnummer fnr, String saksnr) {
        return uri(getBaseUri(), VEDLEGG_DEV_PATH, queryParams(SAKSNUMMER, saksnr, FNR, fnr.value()));
    }

    @Override
    public URI pingURI() {
        return uri(getBaseUri(), DEFAULT_PING_PATH);
    }


    public URI minidialogURI() {
        return uri(getBaseUri(), "/me/minidialoger/spm");

    }

}
