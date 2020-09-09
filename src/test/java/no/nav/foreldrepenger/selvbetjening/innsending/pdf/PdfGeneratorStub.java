package no.nav.foreldrepenger.selvbetjening.innsending.pdf;

import java.io.ByteArrayOutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import no.nav.foreldrepenger.selvbetjening.error.UnexpectedInputException;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.tilbakebetaling.TilbakebetalingUttalelse;

public class PdfGeneratorStub implements PdfGenerator {

    public byte[] generate(String tekst) {

        try (PDDocument document = new PDDocument();
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.setFont(PDType1Font.COURIER, 12);
            contentStream.beginText();
            contentStream.showText(tekst);
            contentStream.endText();
            contentStream.close();
            document.save(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new UnexpectedInputException("Kunne ikke lage PDF", e);
        }
    }

    @Override
    public byte[] generate(TilbakebetalingUttalelse uttalelse) {
        return generate(uttalelse.getBrukerTekst().getTekst());
    }

    @Override
    public String ping() {
        return "HEI";
    }
}
