package no.nav.foreldrepenger.selvbetjening.rest.attachments;

import org.junit.Test;

import no.nav.foreldrepenger.selvbetjening.rest.attachments.DefaultPdf2ImageConverter;
import no.nav.foreldrepenger.selvbetjening.rest.attachments.PDFPageSplitter;
import no.nav.foreldrepenger.selvbetjening.rest.attachments.Pdf2ImageConverter;

import javax.imageio.ImageIO;
import java.util.List;

import static org.junit.Assert.*;

public class DefaultPdf2ImageConverterTest {

    @Test
    public void verifyConversion() {
        ImageIO.setUseCache(false);
        PDFPageSplitter splitter = new PDFPageSplitter();
        Pdf2ImageConverter converter = new DefaultPdf2ImageConverter();
        List<byte[]> pages = splitter.split("/pdf/test123.pdf");
        List<byte[]> jpgs = converter.convertToImages(pages);
        jpgs.forEach(jpg-> assertTrue(hasJpgSignature(jpg)));
    }

    private boolean hasJpgSignature(byte[] bytes) {
        return bytes[0] == (byte) 0xFF &&
                bytes[1] == (byte) 0xD8 &&
                bytes[2] == (byte) 0xFF;
    }

}
