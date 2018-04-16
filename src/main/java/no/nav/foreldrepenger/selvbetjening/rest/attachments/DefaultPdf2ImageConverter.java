package no.nav.foreldrepenger.selvbetjening.rest.attachments;

import static java.util.stream.Collectors.toList;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultPdf2ImageConverter implements Pdf2ImageConverter {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultPdf2ImageConverter.class);

    @Override
    public List<byte[]> convertToImages(List<byte[]> pdfPages) {
        LOG.info("Konverterer {} PDF-sider til image", pdfPages.size());
        return pdfPages.stream()
                .map(this::toBufferedImage)
                .map(this::toBytes)
                .collect(toList());
    }

    private BufferedImage toBufferedImage(byte[] page) {
        LOG.info("Konverterer {} bytes til image", page.length);
        try (PDDocument document = PDDocument.load(page)) {
            return new PDFRenderer(document).renderImageWithDPI(0, 300, ImageType.RGB);
        } catch (IOException e) {
            LOG.warn("Kunne ikke konvertere PDF til BufferedImage", e);
            throw new AttachmentConversionException("Kunne ikke konvertere PDF til BufferedImage", e);
        }
    }

    private byte[] toBytes(BufferedImage img) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(img, "jpg", baos);
            LOG.info("Konverterer image {} bytes", img);
            baos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            LOG.warn("Kunne ikke konvertere BufferedImage til bytes", e);
            throw new AttachmentConversionException("Kunne ikke konvertere BufferedImage til bytes", e);
        }
    }
}
