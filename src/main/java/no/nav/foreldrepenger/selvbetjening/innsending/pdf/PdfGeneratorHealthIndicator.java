package no.nav.foreldrepenger.selvbetjening.innsending.pdf;

import no.nav.foreldrepenger.selvbetjening.health.AbstractPingableHealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class PdfGeneratorHealthIndicator extends AbstractPingableHealthIndicator {
    public PdfGeneratorHealthIndicator(PdfGeneratorConnection tjeneste) {
        super(tjeneste);
    }
}
