package no.nav.foreldrepenger.selvbetjening.felles.attachments;

import no.nav.foreldrepenger.selvbetjening.felles.attachments.exceptions.AttachmentConversionException;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static java.awt.image.AffineTransformOp.TYPE_BILINEAR;

public class ImageScaler {

    public static byte[] downToA4(byte[] origImage, String format) {
        final PDRectangle A4 = PDRectangle.A4;

        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(origImage));

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

        BufferedImage rotatedImage = new BufferedImage(image.getHeight(), image.getWidth(), image.getType());
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(90), image.getHeight() / 2f, image.getHeight() / 2f);
        AffineTransformOp op = new AffineTransformOp(transform, TYPE_BILINEAR);
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
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(img, format, baos);
            return baos.toByteArray();
        }
    }

}
