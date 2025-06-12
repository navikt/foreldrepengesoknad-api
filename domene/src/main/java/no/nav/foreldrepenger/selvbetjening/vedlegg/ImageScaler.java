package no.nav.foreldrepenger.selvbetjening.vedlegg;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Predicate;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

class ImageScaler {

    private static final Logger LOG = LoggerFactory.getLogger(ImageScaler.class);

    private ImageScaler() {

    }

    static void pdfFraBilde(PDDocument doc, byte[] image, MediaType mediaType) {
        var pdPage = new PDPage(PDRectangle.A4);
        doc.addPage(pdPage);
        try (var pdPageContentStream = new PDPageContentStream(doc, pdPage)) {
            var bufferedImage = ImageIO.read(new ByteArrayInputStream(image));
            var matrix = matrixFromImage(pdPage, bufferedImage);
            PDImageXObject pdImageXObject;
            if (mediaType.equals(MediaType.IMAGE_JPEG)) {
                pdImageXObject = JPEGFactory.createFromImage(doc, bufferedImage);
            } else {
                pdImageXObject = LosslessFactory.createFromImage(doc, bufferedImage);
            }
            pdPageContentStream.drawImage(pdImageXObject, matrix);
        } catch (IOException e) {
            throw new AttachmentConversionException("Konvertering av vedlegg feilet", e);
        }
    }

    static Matrix matrixFromImage(PDPage pdPage, BufferedImage bufferedImage) {
        if (bufferedImage.getType() == BufferedImage.TYPE_CUSTOM) {
            LOG.info("BufferedImage er TYPE_CUSTOM"); // ukjent type, logger i tilfelle problemer med konvertering
        }
        var bildedimensjon = new Bildedimensjon(bufferedImage.getWidth(), bufferedImage.getHeight());
        return bildedimensjon.transform(pdPage);
    }

    private static class Bildedimensjon {
        private final float width;
        private final float height;
        private final boolean rotert;

        private Bildedimensjon(float width, float height, boolean rotert) {
            this.width = width;
            this.height = height;
            this.rotert = rotert;
        }

        public Bildedimensjon(int width, int height) {
            this(width, height, false);
        }

        public Matrix transform(PDPage pdPage) {
            var skalertBildedimensjon = roterOgSkalerNed(pdPage.getMediaBox().getWidth(), pdPage.getMediaBox().getHeight());
            var transform = new AffineTransform(skalertBildedimensjon.width,
                0f,
                0f,
                skalertBildedimensjon.height,
                pdPage.getMediaBox().getLowerLeftX(),
                pdPage.getMediaBox().getUpperRightY() - skalertBildedimensjon.height);

            var matrix = new Matrix(transform);

            if (skalertBildedimensjon.rotert) {
                matrix.translate(1f, 0f); // Flytt bildet 1 gang (width) til høyre på x-aksen
                matrix.rotate(Math.toRadians(90.0));
                pdPage.setRotation(90);
            }

            return matrix;
        }

        private Bildedimensjon roterOgSkalerNed(float pageSizeWidth, float pageSizeHeight) {
            var skalRoteres = width > height && width > pageSizeWidth;
            if (skalRoteres) {
                return new Bildedimensjon(height, width, true).skalertDimensjon(pageSizeWidth, pageSizeHeight);
            } else {
                return skalertDimensjon(pageSizeWidth, pageSizeHeight);
            }
        }

        private Bildedimensjon skalertDimensjon(float pageSizeWidth, float pageSizeHeight) {
            var newWidth = this.width;
            var newHeight = this.height;
            Predicate<Bildedimensjon> behovForNedskaleringPortrett = bd -> !bd.rotert && (pageSizeWidth < bd.width || pageSizeHeight < bd.height);
            Predicate<Bildedimensjon> behovForNedskaleringLandskap = bd -> bd.rotert && (pageSizeWidth < bd.height || pageSizeHeight < bd.width);
            if (behovForNedskaleringLandskap.or(behovForNedskaleringPortrett).test(this)) {
                // Skaler og ivareta ratio
                var widthRatio = pageSizeWidth / this.width;
                var heightRatio = pageSizeHeight / this.height;
                var scale = Math.min(widthRatio, heightRatio);
                newWidth *= scale;
                newHeight *= scale;
            }
            return new Bildedimensjon(newWidth, newHeight, this.rotert);
        }
    }

}
