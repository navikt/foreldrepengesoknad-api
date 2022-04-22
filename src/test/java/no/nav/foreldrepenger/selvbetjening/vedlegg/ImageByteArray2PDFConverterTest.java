package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.IMAGE_GIF;

import java.util.Arrays;

import org.apache.tika.Tika;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class ImageByteArray2PDFConverterTest {
    private static final byte[] PDFSIGNATURE = { 0x25, 0x50, 0x44, 0x46 };

    private static Image2PDFConverter converter;

    @BeforeAll
    public static void before() {
        converter = new Image2PDFConverter();
    }

    @Test
    public void jpgConvertsToPdf() {
        assertTrue(isPdf(converter.convert("pdf/jks.jpg")));
    }

    @Test
    public void pngConvertsToPdf() {
        assertTrue(isPdf(converter.convert("pdf/nav-logo.png")));
    }

    @Test
    public void gifFailsfWhenNotConfigured() {
        assertThrows(AttachmentTypeUnsupportedException.class, () -> converter.convert("pdf/loading.gif"));
    }

    @Test
    public void gifConvertsToPdfWhenConfigured() {
        assertTrue(isPdf(new Image2PDFConverter(IMAGE_GIF).convert("pdf/loading.gif")));
    }

    @Test
    public void pdfRemainsUnchanged() {
        assertEquals(MediaType.APPLICATION_PDF,
                MediaType.valueOf(new Tika().detect(converter.convert("pdf/test123.pdf"))));
    }

    @Test
    public void whateverElseIsNotAllowed() {
        assertThrows(AttachmentTypeUnsupportedException.class, () -> converter.convert(new byte[] { 1, 2, 3, 4 }));
    }

    static boolean isPdf(byte[] fileContents) {
        return Arrays.equals(Arrays.copyOfRange(fileContents, 0, PDFSIGNATURE.length), PDFSIGNATURE);
    }

    @Test
    public void pdfManyPages() {
        var content = converter.convert("pdf/spring-framework-reference.pdf");
        assertThat(content).isNotEmpty();
    }
}
