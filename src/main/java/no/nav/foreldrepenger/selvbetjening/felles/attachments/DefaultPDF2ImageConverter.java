package no.nav.foreldrepenger.selvbetjening.felles.attachments;

import static java.util.stream.Collectors.toList;
import static org.apache.pdfbox.rendering.ImageType.RGB;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.felles.attachments.exceptions.AttachmentConversionException;

@Component
public class DefaultPDF2ImageConverter implements PDF2ImageConverter {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultPDF2ImageConverter.class);

    private static final int DPI = 300;
    private static final int FIRST_PAGE = 0;

    @Override
    public List<byte[]> convertToImages(List<byte[]> pdfPages) {
        LOG.info("Konverterer {} PDF-side(r) til bilde(r)", pdfPages.size());
        return pdfPages.stream()
                .map(DefaultPDF2ImageConverter::toBufferedImage)
                .map(DefaultPDF2ImageConverter::toBytes)
                .collect(toList());
    }

    private static BufferedImage toBufferedImage(byte[] page) {
        LOG.info("Konverterer {} bytes til image", page.length);
        PDDocument document = null;
        try {
            document = PDDocument.load(page);

            checkPageDimensions(document);

            return new PDFRenderer(document).renderImageWithDPI(FIRST_PAGE, DPI, RGB);
        } catch (IOException e) {
            LOG.warn("Kunne ikke konvertere PDF til image", e);
            throw new AttachmentConversionException("Kunne ikke konvertere PDF til bilde", e);
        } finally {
            PDFUtils.closeQuietly(document);
        }
    }

    private static void checkPageDimensions(PDDocument document) {
        PDPage pdfPage = document.getPage(FIRST_PAGE);
        PDRectangle cropbBox = pdfPage.getCropBox();
        float widthPt = cropbBox.getWidth();
        float heightPt = cropbBox.getHeight();
        int widthPx = Math.round(widthPt * (DPI/72f));
        int heightPx = Math.round(heightPt * (DPI/72f));

        LOG.info("PDF page dimensions: {} x {}", widthPx, heightPx);
    }

    private static byte[] toBytes(BufferedImage img) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.setUseCache(false);
            ImageIO.write(img, "jpg", baos);
            LOG.info("Konverterte bilde til {}", img);
            baos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            LOG.warn("Kunne ikke konvertere image til bytes", e);
            throw new AttachmentConversionException("Kunne ikke konvertere bilde til bytes", e);
        }
    }
}
