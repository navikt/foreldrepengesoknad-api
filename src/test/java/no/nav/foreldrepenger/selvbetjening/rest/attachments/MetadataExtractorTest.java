package no.nav.foreldrepenger.selvbetjening.rest.attachments;

import static org.junit.Assert.assertEquals;

import no.nav.foreldrepenger.selvbetjening.SlowTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(SlowTests.class)
public class MetadataExtractorTest {

    @Test
    public void testPages() throws Exception {
        PDFMetadata metadata = new PDFMetadataExtractor().metadata("pdf/spring-framework-reference.pdf");
        assertEquals(metadata.pages(), 798);
    }
}
