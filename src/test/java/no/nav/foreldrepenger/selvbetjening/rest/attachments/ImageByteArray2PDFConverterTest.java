package no.nav.foreldrepenger.selvbetjening.rest.attachments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.IMAGE_GIF;

import java.io.IOException;
import java.util.Arrays;

import no.nav.foreldrepenger.selvbetjening.SlowTests;
import org.apache.tika.Tika;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.http.MediaType;

import no.nav.foreldrepenger.selvbetjening.rest.attachments.exceptions.AttachmentTypeUnsupportedException;
import no.nav.foreldrepenger.selvbetjening.rest.attachments.exceptions.AttachmentsTooLargeException;

@Category(SlowTests.class)
public class ImageByteArray2PDFConverterTest {
    private static final byte[] PDFSIGNATURE = { 0x25, 0x50, 0x44, 0x46 };

    private static Image2PDFConverter converter;

    @BeforeClass
    public static void before() throws IOException {
        converter = new Image2PDFConverter(new PDFPageSplitter(), new DefaultPDF2ImageConverter());
    }

    @Test
    public void jpgConvertsToPdf() {
        assertTrue(isPdf(converter.convert("pdf/jks.jpg")));
    }

    @Test
    public void pngConvertsToPdf() {
        assertTrue(isPdf(converter.convert("pdf/nav-logo.png")));
    }

    @Test(expected = AttachmentTypeUnsupportedException.class)
    public void gifFailsfWhenNotConfigured() {
        converter.convert("pdf/loading.gif");
    }

    @Test
    public void gifConvertsToPdfWhenConfigured() throws IOException {
        assertTrue(isPdf(
                new Image2PDFConverter(new PDFPageSplitter(),
                        new DefaultPDF2ImageConverter(), IMAGE_GIF)
                                .convert("pdf/loading.gif")));
    }

    @Test
    public void pdfRemainsUnchanged() {
        assertEquals(MediaType.APPLICATION_PDF,
                MediaType.valueOf(new Tika().detect(converter.convert("pdf/test123.pdf"))));
    }

    @Test(expected = AttachmentTypeUnsupportedException.class)
    public void whateverElseIsNotAllowed() {
        converter.convert(new byte[] { 1, 2, 3, 4 });
    }

    static boolean isPdf(byte[] fileContents) {
        return Arrays.equals(Arrays.copyOfRange(fileContents, 0, PDFSIGNATURE.length), PDFSIGNATURE);
    }

    @Test(expected = AttachmentsTooLargeException.class)
    public void testTooLargePdfFile() throws Exception {
        converter.convert("pdf/spring-framework-reference.pdf");

    }
}
