package no.nav.foreldrepenger.selvbetjening.innsending.pdf;

import no.nav.foreldrepenger.selvbetjening.http.Pingable;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.tilbakebetaling.TilbakebetalingUttalelse;

public interface PdfGenerator extends Pingable {

    byte[] generate(TilbakebetalingUttalelse uttalelse);

}
