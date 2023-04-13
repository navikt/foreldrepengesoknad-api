package no.nav.foreldrepenger.selvbetjening.innsending.pdf;

import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.net.URI;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

@ConfigurationProperties(prefix = "fppdfgen")
public class PdfGeneratorConfig extends AbstractConfig {

    private static final String TILBAKEBETALING_UTTALELSE = "api/v1/genpdf/tilbakebetaling/uttalelse";
    private static final String DEFAULT_PING_PATH = "is_alive";

    public PdfGeneratorConfig(@DefaultValue("http://fppdfgen") URI uri,
                              @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
    }

    @Override
    protected URI pingURI() {
        return uri(getBaseUri(), DEFAULT_PING_PATH);
    }

    URI tilbakebetalingURI() {
        return uri(getBaseUri(), TILBAKEBETALING_UTTALELSE);
    }

}
