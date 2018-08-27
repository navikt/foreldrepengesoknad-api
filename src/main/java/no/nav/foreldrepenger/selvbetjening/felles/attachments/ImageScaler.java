package no.nav.foreldrepenger.selvbetjening.felles.attachments;

import no.nav.foreldrepenger.selvbetjening.felles.attachments.exceptions.AttachmentConversionException;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageScaler {

    public static byte[] downToA4(byte[] origImage, String format) {
        final PDRectangle A4 = PDRectangle.A4;

        try {
            BufferedImage awtImage = ImageIO.read(new ByteArrayInputStream(origImage));
            Dimension pdfPageDim = new Dimension((int) A4.getWidth(), (int) A4.getHeight());
            Dimension origDim = new Dimension(awtImage.getWidth(), awtImage.getHeight());
            Dimension newDim = getScaledDimension(origDim, pdfPageDim);

            if (newDim.equals(origDim)) {
                return origImage;
            } else {
                BufferedImage scaledImg = scaleDown(awtImage, newDim);
                return toBytes(scaledImg, format);
            }
        } catch (IOException ex) {
            throw new AttachmentConversionException("Konvertering av vedlegg feilet", ex);
        }
    }

    private static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        if (original_width > bound_width) {
            new_width = bound_width;
            new_height = (new_width * original_height) / original_width;
        }

        if (new_height > bound_height) {
            new_height = bound_height;
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }

    private static BufferedImage scaleDown(BufferedImage origImage, Dimension newDim) {
        int newWidth = (int) newDim.getWidth();
        int newHeight = (int) newDim.getHeight();
        Image tempImg = origImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage scaledImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
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
