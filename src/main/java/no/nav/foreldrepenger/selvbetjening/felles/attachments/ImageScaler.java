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

    public static byte[] toA4(byte[] image, String format) {
        final PDRectangle A4 = PDRectangle.A4;

        try {
            BufferedImage awtImage = ImageIO.read(new ByteArrayInputStream(image));
            Dimension pdfPageDim = new Dimension((int) A4.getWidth(), (int) A4.getHeight());
            Dimension imageDim = new Dimension(awtImage.getWidth(), awtImage.getHeight());
            Dimension newDim = getScaledDimension(imageDim, pdfPageDim);
            int newWidth = (int) newDim.getWidth();
            int newHeight = (int) newDim.getHeight();

            BufferedImage newImg = new BufferedImage(newWidth, newHeight, awtImage.getType());
            Graphics2D g = newImg.createGraphics();
            g.setComposite(AlphaComposite.Src);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawImage(awtImage, 0, 0, newWidth, newHeight, null);
            g.dispose();
            return toBytes(newImg, format);
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

    private static byte[] toBytes(BufferedImage img, String format) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(img, format, baos);
            return baos.toByteArray();
        }
    }

}
