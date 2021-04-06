package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static java.awt.image.AffineTransformOp.TYPE_BILINEAR;
import static java.awt.image.BufferedImage.TYPE_CUSTOM;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.selvbetjening.error.AttachmentConversionException;

class ImageScaler {

    private static final Logger LOG = LoggerFactory.getLogger(ImageScaler.class);

    private ImageScaler() {

    }

    static byte[] downToA4(byte[] origImage, String format) {
        final PDRectangle A4 = PDRectangle.A4;

        try {
            var image = ImageIO.read(new ByteArrayInputStream(origImage));
            image = rotatePortrait(image);
            Dimension pdfPageDim = new Dimension((int) A4.getWidth(), (int) A4.getHeight());
            Dimension origDim = new Dimension(image.getWidth(), image.getHeight());
            Dimension newDim = getScaledDimension(origDim, pdfPageDim);

            if (newDim.equals(origDim)) {
                return origImage;
            } else {
                BufferedImage scaledImg = scaleDown(image, newDim);
                return toBytes(scaledImg, format);
            }
        } catch (IOException ex) {
            throw new AttachmentConversionException("Konvertering av vedlegg feilet", ex);
        }
    }

    private static BufferedImage rotatePortrait(BufferedImage image) {
        if (image.getHeight() >= image.getWidth()) {
            return image;
        }
        if (image.getType() == TYPE_CUSTOM) {
            LOG.info("Kan ikke rotere bilde med ukjent type");
            return image;

        }

        var rotatedImage = new BufferedImage(image.getHeight(), image.getWidth(), image.getType());
        var transform = new AffineTransform();
        transform.rotate(Math.toRadians(90), image.getHeight() / 2f, image.getHeight() / 2f);
        var op = new AffineTransformOp(transform, TYPE_BILINEAR);
        rotatedImage = op.filter(image, rotatedImage);
        return rotatedImage;
    }

    private static Dimension getScaledDimension(Dimension imgSize, Dimension a4) {
        int originalWidth = imgSize.width;
        int originalHeight = imgSize.height;
        int a4Width = a4.width;
        int a4Height = a4.height;
        int newWidth = originalWidth;
        int newHeight = originalHeight;

        if (originalWidth > a4Width) {
            newWidth = a4Width;
            newHeight = (newWidth * originalHeight) / originalWidth;
        }

        if (newHeight > a4Height) {
            newHeight = a4Height;
            newWidth = (newHeight * originalWidth) / originalHeight;
        }

        return new Dimension(newWidth, newHeight);
    }

    private static BufferedImage scaleDown(BufferedImage origImage, Dimension newDim) {
        int newWidth = (int) newDim.getWidth();
        int newHeight = (int) newDim.getHeight();
        Image tempImg = origImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage scaledImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) scaledImg.getGraphics();
        g.drawImage(tempImg, 0, 0, null);
        g.dispose();
        return scaledImg;
    }

    private static byte[] toBytes(BufferedImage img, String format) throws IOException {
        try (var baos = new ByteArrayOutputStream()) {
            ImageIO.write(img, format, baos);
            return baos.toByteArray();
        }
    }

}
