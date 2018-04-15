package no.nav.foreldrepenger.selvbetjening.rest.util;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.util.List;

import static org.junit.Assert.*;

public class DefaultPdf2ImageConverterTest {

    @Test
    public void verfiyConversion() {
        ImageIO.setUseCache(false);
        PDFPageSplitter splitter = new PDFPageSplitter();
        Pdf2ImageConverter converter = new DefaultPdf2ImageConverter();
        List<byte[]> pages = splitter.split("/pdf/test123.pdf");
        List<byte[]> pngs = converter.convertToImages(pages);
        pngs.stream().forEach(png-> assertTrue(hasPngSignature(png)));
    }

    private boolean hasPngSignature(byte[] bytes) {
        return bytes[0] == (byte) 0x89 &&
                bytes[1] == (byte) 0x50 &&
                bytes[2] == (byte) 0x4E &&
                bytes[3] == (byte) 0x47 &&
                bytes[4] == (byte) 0x0D &&
                bytes[5] == (byte) 0x0A &&
                bytes[6] == (byte) 0x1A &&
                bytes[7] == (byte) 0x0A;
    }

}
