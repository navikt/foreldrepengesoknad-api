package no.nav.foreldrepenger.selvbetjening.innsending.pdf;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.TilbakebetalingUttalelseDto;

public interface PdfGenerator {

    byte[] generate(TilbakebetalingUttalelseDto uttalelse);

}
