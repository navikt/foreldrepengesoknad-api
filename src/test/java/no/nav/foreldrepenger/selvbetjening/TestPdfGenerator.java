package no.nav.foreldrepenger.selvbetjening;

import org.junit.Test;

import no.nav.foreldrepenger.selvbetjening.rest.util.ByteArray2PdfConverter;

public class TestPdfGenerator {
    @Test
    public void convert() throws Exception {
        byte[] bytes = new ByteArray2PdfConverter().convert("pdf/jks.jpg");
    }
}
