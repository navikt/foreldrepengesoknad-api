package no.nav.foreldrepenger.selvbetjening;

import static org.junit.Assert.*;

import no.nav.foreldrepenger.selvbetjening.rest.util.UnsupportedAttachmentTypeException;
import org.junit.Test;

import no.nav.foreldrepenger.selvbetjening.rest.util.ImageByteArray2PdfConverter;

import java.util.Arrays;

public class ImageByteArray2PdfConverterTest {
    private static final byte[] pdfSignature = {0x25, 0x50, 0x44, 0x46};

    @Test
    public void jpgConvertsToPdf() {
        byte[] converted = new ImageByteArray2PdfConverter().convert("pdf/jks.jpg");
        assertTrue(isPdf(converted));
    }

    @Test
    public void pngConvertsToPdf() {
        byte[] converted = new ImageByteArray2PdfConverter().convert("pdf/nav-logo.png");
        assertTrue(isPdf(converted));
    }

    @Test
    public void pdfRemainsUnchanged() {
        byte[] converted = new ImageByteArray2PdfConverter().convert("pdf/test.pdf");
        assertTrue(isPdf(converted));
    }

    @Test(expected = UnsupportedAttachmentTypeException.class)
    public void whateverElseIsNotAllowed() {
        byte[] bytes = {1, 2, 3 ,4};
        new ImageByteArray2PdfConverter().convert(bytes);
    }

    private boolean isPdf(byte[] fileContents) {
        byte[] firstFourBytes = Arrays.copyOfRange(fileContents, 0, 4);
        return Arrays.equals(firstFourBytes, pdfSignature);
    }
}
