package no.nav.foreldrepenger.selvbetjening.innsending.pdf;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.tilbakebetaling.TilbakebetalingUttalelse;

public interface PdfGenerator {

    byte[] generate(TilbakebetalingUttalelse uttalelse);

}
