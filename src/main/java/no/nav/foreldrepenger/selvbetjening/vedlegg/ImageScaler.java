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
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.util.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ImageScaler {

    private static final Logger LOG = LoggerFactory.getLogger(ImageScaler.class);

    private ImageScaler() {

    }

    static byte[] downToA4(byte[] origImage, String format) {
        try {

            BufferedImage orig = ImageIO.read(new ByteArrayInputStream(origImage));

            //return resizedImage;
            return null;

        } catch (IOException ex) {
            throw new AttachmentConversionException("Konvertering av vedlegg feilet", ex);
        }
    }

    static void pdfFraBilde(PDDocument doc, byte[] bilde) {
        PDPage pdPage = new PDPage(PDRectangle.A4);
        doc.addPage(pdPage);

        try (var pdPageContentStream = new PDPageContentStream(doc, pdPage)) {
            var bufferedImage = ImageIO.read(new ByteArrayInputStream(bilde));
            var bildedimensjon = new Bildedimensjon(bufferedImage.getWidth(), bufferedImage.getHeight());
            var matrix = bildedimensjon.transform(pdPage);
            var pdImageXObject = LosslessFactory.createFromImage(doc, bufferedImage);
            pdPageContentStream.drawImage(pdImageXObject, matrix);
        } catch (Throwable e) {
            long størrelse = bilde.length;
            throw new IllegalStateException("Konvertering av bilde med størrelse " + størrelse + " bytes feilet", e);
        }
    }

    private static class Bildedimensjon {
        private static final Logger LOG = LoggerFactory.getLogger(Bildedimensjon.class);
        private final float width;
        private final float height;
        private final boolean rotert;

        public Bildedimensjon(float width, float height, boolean rotert) {
            this.width = width;
            this.height = height;
            this.rotert = rotert;
        }

        public Bildedimensjon(int width, int height) {
            this(width, height, false);
        }

        public Matrix transform(PDPage pdPage) {
            var skalertBildedimensjon = roterOgSkalerNed(pdPage.getMediaBox().getWidth(), pdPage.getMediaBox().getHeight());
            var transform = new AffineTransform(
                skalertBildedimensjon.width,
                0f,
                0f,
                skalertBildedimensjon.height,
                pdPage.getMediaBox().getLowerLeftX(),
                pdPage.getMediaBox().getUpperRightY() - skalertBildedimensjon.height
            );

            var matrix = new Matrix(transform);

            if (skalertBildedimensjon.rotert) {
                matrix.translate(1f, 0f); // Flytt bildet 1 gang (width) til høyre på x-aksen
                matrix.rotate(Math.toRadians(90.0));
                pdPage.setRotation(90);
            }

            return matrix;
        }

        private Bildedimensjon roterOgSkalerNed(float pageSizeWidth, float pageSizeHeight) {
            boolean skalRoteres = width > height && width > pageSizeWidth;
            if (skalRoteres) {
                return new Bildedimensjon(height, width, true).skalertDimensjon(pageSizeWidth, pageSizeHeight);
            } else {
                return skalertDimensjon(pageSizeWidth, pageSizeHeight);
            }
        }

        private Bildedimensjon skalertDimensjon(float pageSizeWidth, float pageSizeHeight) {
            float width = this.width;
            float height = this.height;
            LOG.info("Bildedimensjon før scale: width {}, height {}, rotert {}", width, height, rotert);
            Predicate<Bildedimensjon> behovForNedskaleringPortrett = bd -> !bd.rotert && (pageSizeWidth < bd.width || pageSizeHeight < bd.height);
            Predicate<Bildedimensjon> behovForNedskaleringLandskap = bd -> bd.rotert && (pageSizeWidth < bd.height || pageSizeHeight < bd.width);
            if (behovForNedskaleringLandskap.or(behovForNedskaleringPortrett).test(this)) {
                // Skaler og ivareta ratio
                float widthRatio = pageSizeWidth / this.width;
                float heightRatio = pageSizeHeight / this.height;
                float scale = Math.min(widthRatio, heightRatio);
                width *= scale;
                height *= scale;
            }

            LOG.info("Bildedimensjon: width {}, height {}, rotert {}", width, height, rotert);
            return new Bildedimensjon(width, height, this.rotert);
        }
    }

}
