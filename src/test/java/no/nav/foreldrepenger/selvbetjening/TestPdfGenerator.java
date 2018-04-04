package no.nav.foreldrepenger.selvbetjening;

import org.junit.Test;

import no.nav.foreldrepenger.selvbetjening.rest.util.ImageByteArray2PdfConverter;

public class TestPdfGenerator {
    @Test
    public void convert() throws Exception {
        byte[] bytes = new ImageByteArray2PdfConverter().convert("pdf/jks.jpg");
    }
}
