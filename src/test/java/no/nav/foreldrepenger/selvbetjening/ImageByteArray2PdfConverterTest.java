package no.nav.foreldrepenger.selvbetjening;

import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.IMAGE_GIF;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

import no.nav.foreldrepenger.selvbetjening.rest.util.ImageByteArray2PdfConverter;
import no.nav.foreldrepenger.selvbetjening.rest.util.UnsupportedAttachmentTypeException;

public class ImageByteArray2PdfConverterTest {
    private static final byte[] PDFSIGNATURE = { 0x25, 0x50, 0x44, 0x46 };

    @Test
    public void jpgConvertsToPdf() {
        assertTrue(isPdf(new ImageByteArray2PdfConverter().convert("pdf/jks.jpg")));
    }

    @Test
    public void pngConvertsToPdf() {
        assertTrue(isPdf(new ImageByteArray2PdfConverter().convert("pdf/nav-logo.png")));
    }

    @Test(expected = UnsupportedAttachmentTypeException.class)
    public void gifConvertsToPdfWhenConfigured() {
        new ImageByteArray2PdfConverter().convert("pdf/loading.gif");
    }

    @Test
    public void gifFailsfWhenNotConfigured() {
        assertTrue(isPdf(new ImageByteArray2PdfConverter(IMAGE_GIF).convert("pdf/loading.gif")));
    }

    @Test
    public void pdfRemainsUnchanged() {
        assertTrue(isPdf(new ImageByteArray2PdfConverter().convert("pdf/test.pdf")));
    }

    @Test(expected = UnsupportedAttachmentTypeException.class)
    public void whateverElseIsNotAllowed() {
        new ImageByteArray2PdfConverter().convert(new byte[] { 1, 2, 3, 4 });
    }

    private static boolean isPdf(byte[] fileContents) {
        return Arrays.equals(Arrays.copyOfRange(fileContents, 0, PDFSIGNATURE.length), PDFSIGNATURE);
    }

    @Ignore
    public void jpgToPdf() throws Exception {
        OutputStream stream = new FileOutputStream("pdf/OUTPUT.pdf");
        stream.write(new ImageByteArray2PdfConverter().convert("pdf/INPUT.jpg"));
        stream.close();
    }
}
