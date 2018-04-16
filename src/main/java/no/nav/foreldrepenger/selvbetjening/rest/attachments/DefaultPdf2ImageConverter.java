package no.nav.foreldrepenger.selvbetjening.rest.attachments;

import static java.util.stream.Collectors.toList;
import static org.apache.pdfbox.rendering.ImageType.RGB;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultPdf2ImageConverter implements Pdf2ImageConverter {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultPdf2ImageConverter.class);

    @Override
    public List<byte[]> convertToImages(List<byte[]> pdfPages) {
        LOG.info("Konverterer {} PDF-side(r) til image", pdfPages.size());
        return pdfPages.stream()
                .map(DefaultPdf2ImageConverter::toBufferedImage)
                .map(DefaultPdf2ImageConverter::toBytes)
                .collect(toList());
    }

    private static BufferedImage toBufferedImage(byte[] page) {
        LOG.info("Konverterer {} bytes til image", page.length);
        try (PDDocument document = PDDocument.load(page)) {
            return new PDFRenderer(document).renderImageWithDPI(0, 300, RGB);
        } catch (IOException e) {
            LOG.warn("Kunne ikke konvertere PDF til image", e);
            throw new AttachmentConversionException("Kunne ikke konvertere PDF til image", e);
        }
    }

    private static byte[] toBytes(BufferedImage img) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.setUseCache(false);
            ImageIO.write(img, "jpg", baos);
            LOG.info("Konverterte image til {}", img);
            baos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            LOG.warn("Kunne ikke konvertere image til bytes", e);
            throw new AttachmentConversionException("Kunne ikke konvertere image til bytes", e);
        }
    }
}
