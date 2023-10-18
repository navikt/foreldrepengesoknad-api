package no.nav.foreldrepenger.selvbetjening.vedlegg;

import org.apache.tika.Tika;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekkerTest.fraResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.IMAGE_GIF;

class ImageByteArray2PDFConverterTest {
    private static final byte[] PDFSIGNATURE = { 0x25, 0x50, 0x44, 0x46 };

    private static Image2PDFConverter converter;

    @BeforeAll
    static void before() {
        converter = new Image2PDFConverter();
    }

    @Test
    void jpgConvertsToPdf() {
        assertTrue(isPdf(converter.convert(fraResource("pdf/jks.jpg"))));
    }

    @Test
    void pngConvertsToPdf() {
        assertTrue(isPdf(converter.convert(fraResource("pdf/nav-logo.png"))));
    }

    @Test
    void gifFailsfWhenNotConfigured() {
        assertThrows(AttachmentTypeUnsupportedException.class, () -> converter.convert(fraResource("pdf/loading.gif")));
    }

    @Test
    void gifConvertsToPdfWhenConfigured() {
        assertTrue(isPdf(new Image2PDFConverter(List.of(IMAGE_GIF)).convert(fraResource("pdf/loading.gif"))));
    }

    @Test
    void pdfRemainsUnchanged() {
        assertEquals(MediaType.APPLICATION_PDF,
                MediaType.valueOf(new Tika().detect(converter.convert(fraResource("pdf/test123.pdf")))));
    }

    @Test
    void whateverElseIsNotAllowed() {
        assertThrows(AttachmentTypeUnsupportedException.class, () -> converter.convert(new byte[] { 1, 2, 3, 4 }));
    }

    static boolean isPdf(byte[] fileContents) {
        return Arrays.equals(Arrays.copyOfRange(fileContents, 0, PDFSIGNATURE.length), PDFSIGNATURE);
    }

    @Test
    void pdfManyPages() {
        var content = converter.convert(fraResource("pdf/spring-framework-reference.pdf"));
        assertThat(content).isNotEmpty();
    }
}
