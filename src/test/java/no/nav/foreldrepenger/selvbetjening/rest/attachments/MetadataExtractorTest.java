package no.nav.foreldrepenger.selvbetjening.rest.attachments;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MetadataExtractorTest {

    @Test
    public void testPages() throws Exception {
        Metadata metadata = new PDFMetadataExtractor().metadata("pdf/spring-framework-reference.pdf");
        assertEquals(metadata.pages(), 798);
    }
}
