package no.nav.foreldrepenger.selvbetjening.innsending.pdf;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "fppdfgen")
public class PdfGeneratorConfig {

    private static final String TILBAKEBETALING_UTTALELSE = "api/v1/genpdf/tilbakebetaling/uttalelse";
    private final URI uri;
    private final boolean enabled;

    public PdfGeneratorConfig(@DefaultValue("http://fppdfgen") URI uri,
                              @DefaultValue("true") boolean enabled) {
        this.uri = uri;
        this.enabled = enabled;
    }

    URI tilbakebetalingURI() {
        return uri(uri, TILBAKEBETALING_UTTALELSE);
    }

    public boolean isEnabled() {
        return enabled;
    }
}
