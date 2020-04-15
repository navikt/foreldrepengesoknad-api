package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.pdf;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractConfig;

@ConfigurationProperties(prefix = "no.nav.foreldrepenger.selvbetjening.api.pdfgenerator")
public class PdfGeneratorConfig extends AbstractConfig {

    private static final String DEFAULT_FPPDFGEN_URI = "http://fppdfgen.default.svc.nais.local/api/v1/genpdf/";
    private static final String TILBAKEBETALING_UTTALELSE = "tilbakebetaling/uttalelse";
    private static final String PING = "/is_alive";

    public PdfGeneratorConfig(@DefaultValue(DEFAULT_FPPDFGEN_URI) URI uri, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
    }

    @Override
    protected URI pingURI() {
        return uri(getUri(), PING);
    }

    URI tilbakebetalingURI() {
        return uri(getUri(), TILBAKEBETALING_UTTALELSE);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[pingURI=" + pingURI() + ", tilbakebetalingURI="
                + tilbakebetalingURI() + "]";
    }
}
