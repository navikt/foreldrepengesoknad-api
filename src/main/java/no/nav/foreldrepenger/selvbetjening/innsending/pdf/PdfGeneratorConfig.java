package no.nav.foreldrepenger.selvbetjening.innsending.pdf;

import static no.nav.foreldrepenger.selvbetjening.util.StringUtil.limit;
import static org.apache.commons.lang3.StringUtils.reverse;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;
import no.nav.foreldrepenger.selvbetjening.http.interceptors.ZoneCrossingAware;

@ConfigurationProperties(prefix = "fppdfgen")
public class PdfGeneratorConfig extends AbstractConfig implements ZoneCrossingAware {

    private static final String TILBAKEBETALING_UTTALELSE = "api/v1/genpdf/tilbakebetaling/uttalelse";
    private static final String DEFAULT_PING_PATH = "is_alive";
    private String key;

    @ConstructorBinding
    public PdfGeneratorConfig(URI uri, @DefaultValue("true") boolean enabled, String key) {
        super(uri, enabled);
        this.key = key;
    }

    @Override
    protected URI pingURI() {
        return uri(getUri(), DEFAULT_PING_PATH);
    }

    URI tilbakebetalingURI() {
        return uri(getUri(), TILBAKEBETALING_UTTALELSE);
    }

    @Override
    public String getKey() {
        return key;
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
