package no.nav.foreldrepenger.selvbetjening.historikk;

import static no.nav.foreldrepenger.selvbetjening.util.StringUtil.limit;
import static org.apache.commons.lang3.StringUtils.reverse;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;
import no.nav.foreldrepenger.selvbetjening.http.interceptors.ZoneCrossingAware;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Fødselsnummer;
import no.nav.foreldrepenger.selvbetjening.util.Pair;

@ConfigurationProperties("historikk")
public class HistorikkConfig extends AbstractConfig implements ZoneCrossingAware {
    private static final String HISTORIKK = "historikk";

    private static final String FNR = "fnr";

    private static final String SAKSNUMMER = "saksnummer";

    private static final String DEFAULT_PING_PATH = "actuator/health/liveness";

    private final String key;

    @ConstructorBinding
    public HistorikkConfig(URI uri, String key, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
        this.key = key;
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

    @Override
    public String getKey() {
        return key;
    }

    public URI minidialogURI() {
        return uri(getUri(), "/me/minidialoger/spm");

    }

    @Override
    public URI zoneCrossingUri() {
        return getUri();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[key=" + limit(reverse(key), 3)
                + ", zoneCrossingUri=" + zoneCrossingUri() + "]";
    }

}
