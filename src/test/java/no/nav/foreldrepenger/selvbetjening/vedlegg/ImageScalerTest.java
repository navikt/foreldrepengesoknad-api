package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.jupiter.api.Test;

class ImageScalerTest {

    @Test
    void imgSmallerThanA4RemainsUnchanged() throws Exception {
        var bufferedImage = getBufferedImage("/pdf/jks.jpg");
        var matrix = ImageScaler.matrixFromImage(new PDPage(PDRectangle.A4), bufferedImage);
        assertThat(matrix.getScaleX()).isEqualTo(bufferedImage.getWidth());
        assertThat(matrix.getScaleY()).isEqualTo(bufferedImage.getHeight());
    }

    @Test
    void imgBiggerThanA4IsScaledDown() throws Exception {
        var origImg = getBufferedImage("/pdf/rdd.png");
        var matrix = ImageScaler.matrixFromImage(new PDPage(PDRectangle.A4), origImg);
        assertThat(matrix.getScaleX()).isLessThan(origImg.getWidth());
        assertThat(matrix.getScaleY()).isLessThan(origImg.getHeight());
    }

    @Test
    void rotateLandscapeToPortrait() throws Exception {
        var pdPage = new PDPage(PDRectangle.A4);
        var origImage = getBufferedImage("/pdf/landscape.jpg");
        ImageScaler.matrixFromImage(pdPage, origImage);
        // rotert
        assertThat(pdPage.getRotation()).isEqualTo(90);
    }

    @Test
    void bufferedImageCustomType() throws Exception {
        var origImage = getBufferedImage("/pdf/png_type_0.png");
        assertDoesNotThrow(() -> ImageScaler.matrixFromImage(new PDPage(PDRectangle.A4), origImage));
    }

    private static BufferedImage getBufferedImage(String imageResource) throws IOException, URISyntaxException {
        var uri = ImageScalerTest.class.getResource(imageResource).toURI();
        var bytes = Files.readAllBytes(Paths.get(uri));
        try (var is = new ByteArrayInputStream(bytes)) {
            return ImageIO.read(is);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
