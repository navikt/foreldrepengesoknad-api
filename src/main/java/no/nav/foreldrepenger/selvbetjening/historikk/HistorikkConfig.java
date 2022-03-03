package no.nav.foreldrepenger.selvbetjening.historikk;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.common.util.Pair;
import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Fødselsnummer;

@ConfigurationProperties("historikk")
public class HistorikkConfig extends AbstractConfig {
    private static final String HISTORIKK = "historikk";

    private static final String FNR = "fnr";

    private static final String SAKSNUMMER = "saksnummer";

    private static final String DEFAULT_PING_PATH = "actuator/health/liveness";


    @ConstructorBinding
    public HistorikkConfig(URI uri, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
    }

    public URI historikkURI() {
        return uri(getUri(), HISTORIKK + "/me/all");
    }

    public URI vedleggURI(String saksnr) {
        return uri(getUri(), HISTORIKK + "/me/manglendevedlegg", queryParams(SAKSNUMMER, saksnr));
    }

    public URI historikkPreprodURI(String fnr) {
        return uri(getUri(), HISTORIKK + "/dev/all", queryParams(FNR, fnr));
    }

    public URI vedleggPreprodURI(Fødselsnummer fnr, String saksnr) {
        return uri(getUri(), HISTORIKK + "/dev/vedlegg",
                queryParams(Pair.of(SAKSNUMMER, saksnr), Pair.of(FNR, fnr.getFnr())));
    }

    @Override
    public URI pingURI() {
        return uri(getUri(), DEFAULT_PING_PATH);
    }


    public URI minidialogURI() {
        return uri(getUri(), "/me/minidialoger/spm");

    }

}
