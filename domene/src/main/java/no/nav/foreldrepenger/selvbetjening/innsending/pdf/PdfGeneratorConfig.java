package no.nav.foreldrepenger.selvbetjening.innsending.pdf;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "pdfgenerator")
public class PdfGeneratorConfig {
    private final URI uri;
    private final String path;
    private final boolean enabled;

    public PdfGeneratorConfig(@DefaultValue("http://fppdfgen") URI uri,
                              @DefaultValue("api/v1/genpdf/tilbakebetaling/uttalelse") String path,
                              @DefaultValue("true") boolean enabled) {
        this.uri = uri;
        this.path = path;
        this.enabled = enabled;
    }

    URI tilbakebetalingURI() {
        return uri(uri, path);
    }

    public boolean isEnabled() {
        return enabled;
    }
}
