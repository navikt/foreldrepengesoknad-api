package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.pdf;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.tilbakebetaling.TilbakebetalingUttalelse;

public interface PdfGenerator {

    byte[] generate(TilbakebetalingUttalelse uttalelse);

}
