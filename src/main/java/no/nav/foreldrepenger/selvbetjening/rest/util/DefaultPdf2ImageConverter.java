package no.nav.foreldrepenger.selvbetjening.rest.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;

import static java.util.stream.Collectors.*;

@Component
public class DefaultPdf2ImageConverter implements Pdf2ImageConverter {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultPdf2ImageConverter.class);

    @Override
    public List<byte[]> convertToImages(List<byte[]> pdfPages) {
        System.out.println("Pages: " + pdfPages.size());
        return pdfPages.stream()
                .map(this::toBufferedImage)
                .map(this::toBytes)
                .collect(toList());
    }

    private BufferedImage toBufferedImage(byte[] page) {
        try (PDDocument document = PDDocument.load(page)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            return pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
        } catch (IOException ex) {
            LOG.warn("Error while converting PDF to BufferedImage: " + ex.getMessage());
            throw new RuntimeException("Error while converting PDF to BufferedImage", ex);
        }
    }

    private byte[] toBytes(BufferedImage img) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(img, "jpg", baos);
            baos.flush();
            return baos.toByteArray();
        } catch (IOException ex) {
            LOG.warn("Error while converting BufferedImage to byte array: " + ex.getMessage());
            throw new RuntimeException("Error while converting BufferedImage to byte array", ex);
        }
    }
}
