package no.nav.foreldrepenger.selvbetjening.vedlegg;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
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

        try {
            java.awt.image.BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bilde));
            Bildedimensjon bildedimensjon = new Bildedimensjon(bufferedImage.getWidth(), bufferedImage.getHeight());

            Matrix matrix = bildedimensjon.transform(pdPage);
            PDImageXObject pdImg = LosslessFactory.createFromImage(doc, bufferedImage);

            try (PDPageContentStream pdPageContentStream = new PDPageContentStream(doc, pdPage)) {
                pdPageContentStream.drawImage(pdImg, matrix);
            }
        } catch (Throwable e) {
            long størrelse = bilde.length;
            throw new IllegalStateException("Konvertering av bilde med størrelse " + størrelse + " bytes feilet", e);
        }
    }

    private static class Bildedimensjon {
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
            Bildedimensjon skalertBildedimensjon = roterOgSkalerNed(pdPage.getMediaBox().getWidth(), pdPage.getMediaBox().getHeight());

            AffineTransform transform = new AffineTransform(
                skalertBildedimensjon.width,
                0f,
                0f,
                skalertBildedimensjon.height,
                pdPage.getMediaBox().getLowerLeftX(),
                pdPage.getMediaBox().getLowerLeftY()
            );

            Matrix matrix = new Matrix(transform);

            if (skalertBildedimensjon.rotert) {
                matrix.translate(1f, 0f); // Flytt bildet 1 gang (width) til høyre på x-aksen
                matrix.rotate(Math.toRadians(90.0));
                pdPage.setRotation(90);
            }

            return matrix;
        }

        private boolean roteres() {
            return width > height;
        }

        private Bildedimensjon roterOgSkalerNed(float pageSizeWidth, float pageSizeHeight) {
            if (roteres()) {
                return new Bildedimensjon(height, width, true).skalertDimensjon(pageSizeWidth, pageSizeHeight);
            } else {
                return skalertDimensjon(pageSizeWidth, pageSizeHeight);
            }
        }

        private Bildedimensjon skalertDimensjon(float pageSizeWidth, float pageSizeHeight) {
            float width = this.width;
            float height = this.height;

            if (width > pageSizeWidth) {
                width = pageSizeWidth;
                height = width * this.height / this.width;
            }
            if (height > pageSizeHeight) {
                height = pageSizeHeight;
                width = height * this.width / this.height;
            }
            return new Bildedimensjon(width, height, this.rotert);
        }
    }

}
