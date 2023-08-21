package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.unit.DataSize;

import no.nav.foreldrepenger.common.error.UnexpectedInputException;
import no.nav.foreldrepenger.common.util.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.vedlegg.DelegerendeVedleggSjekker;
import no.nav.foreldrepenger.selvbetjening.vedlegg.PDFEncryptionVedleggSjekker;
import no.nav.foreldrepenger.selvbetjening.vedlegg.StørrelseVedleggSjekker;
import no.nav.foreldrepenger.selvbetjening.vedlegg.virusscan.ClamAvVirusScanner;

@ExtendWith(MockitoExtension.class)
class KryptertMellomlagringTest {

    @Mock
    ClamAvVirusScanner scanner;
    @Mock
    TokenUtil util;
    @Mock
    Bøtte bøtte;
    private KryptertMellomlagring km;

    @BeforeEach
    void beforeEach() {
        when(util.getSubject()).thenReturn("01010111111");
        var mellomlagring = new InMemoryMellomlagring(bøtte);
        km = new KryptertMellomlagring(mellomlagring,
                new MellomlagringKrypto("passphrase", util),
                new DelegerendeVedleggSjekker(new StørrelseVedleggSjekker(DataSize.ofMegabytes(32), DataSize.ofMegabytes(8)), scanner,
                        new PDFEncryptionVedleggSjekker()));
    }

    @Test
    void TestKryptertSøknad() {
        km.lagreKryptertSøknad("Søknad");
        var lest = km.lesKryptertSøknad();
        assertThat(lest).isPresent();
        assertThat(lest.get()).contains("Søknad");
        km.slettKryptertSøknad();
        assertThat(km.lesKryptertSøknad()).isNotPresent();
    }

    @Test
    void TestKryptertVedlegg() {
        var pdf = generatePdf();
        var original = Attachment.of(new MockMultipartFile("vedlegg", "originalt vedlegg", "application/pdf", pdf));
        km.lagreKryptertVedlegg(original);
        var lest = km.lesKryptertVedlegg(original.getUuid());
        assertThat(lest).isPresent();
        assertEquals(original, lest.get());
        km.slettKryptertVedlegg(original.getUuid());
        assertThat(km.lesKryptertSøknad()).isNotPresent();
    }

    private static byte[] generatePdf() {

        try (var document = new PDDocument(); var baos = new ByteArrayOutputStream()) {

            var page = new PDPage();
            document.addPage(page);

            var contentStream = new PDPageContentStream(document, page);

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER), 12);
            contentStream.beginText();
            contentStream.showText("test");
            contentStream.endText();
            contentStream.close();
            document.save(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new UnexpectedInputException("Kunne ikke lage PDF", e);
        }
    }
}
