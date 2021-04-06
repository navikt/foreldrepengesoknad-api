package no.nav.foreldrepenger.selvbetjening.innsending.pdf;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.health.AbstractPingableHealthIndicator;

@Component
public class PdfGeneratorHealthIndicator extends AbstractPingableHealthIndicator {
    public PdfGeneratorHealthIndicator(PdfGeneratorConnection tjeneste) {
        super(tjeneste);
    }
}
