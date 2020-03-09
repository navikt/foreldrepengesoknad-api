package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.pdf;

import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.net.URI;

@ConfigurationProperties(prefix = "pdfgenerator")
public class PdfGeneratorConfig extends AbstractConfig {

    private static final String TILBAKEBETALING_UTTALELSE = "tilbakebetaling/uttalelse";
    private static final String PING = "/is_alive";

    public PdfGeneratorConfig(URI uri, @DefaultValue("false") boolean enabled) {
        super(uri, enabled);
    }

    @Override
    protected URI pingURI() {
        return uri(getUri(), PING);
    }

    URI tilbakebetalingURI() {
        return uri(getUri(), TILBAKEBETALING_UTTALELSE);
    }
}
