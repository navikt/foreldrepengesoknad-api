package no.nav.foreldrepenger.selvbetjening.rest.json.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import no.nav.foreldrepenger.selvbetjening.rest.util.PDFPageSplitter;

public class PageSplitterTest {

    @Test
    public void testPages() throws Exception {
        List<byte[]> pages = new PDFPageSplitter().split("pdf/spring-framework-reference.pdf");
        assertEquals(798, pages.size());
        for (byte[] page : pages) {
            assertTrue(ImageByteArray2PdfConverterTest.isPdf(page));
        }
    }
}
