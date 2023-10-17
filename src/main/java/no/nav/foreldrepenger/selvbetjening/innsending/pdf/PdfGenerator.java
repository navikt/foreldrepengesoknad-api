package no.nav.foreldrepenger.selvbetjening.innsending.pdf;

public interface PdfGenerator {

    byte[] generate(no.nav.foreldrepenger.selvbetjening.innsending.dto.ettersendelse.TilbakebetalingUttalelseDto uttalelse);

}
