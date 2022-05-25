package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

class ImageScalerTest {

    @Test
    void imgSmallerThanA4RemainsUnchanged() throws Exception {
        URL url = getClass().getResource("/pdf/jks.jpg");
        byte[] orig = Files.readAllBytes(Paths.get(url.toURI()));
        byte[] scaled = ImageScaler.downToA4(orig, "jpg");
        assertThat(scaled).hasSameSizeAs(orig);
    }

    @Test
    void imgBiggerThanA4IsScaledDown() throws Exception {
        URL url = getClass().getResource("/pdf/rdd.png");
        byte[] orig = Files.readAllBytes(Paths.get(url.toURI()));
        byte[] scaled = ImageScaler.downToA4(orig, "jpg");
        BufferedImage origImg = fromBytes(orig);
        BufferedImage scaledImg = fromBytes(scaled);
        assertThat(scaledImg.getWidth()).isLessThan(origImg.getWidth());
        assertThat(scaledImg.getHeight()).isLessThan(origImg.getHeight());
    }

    @Test
    void scaledImgHasRetainedFormat() throws Exception {
        URL url = getClass().getResource("/pdf/rdd.png");
        final byte[] orig = Files.readAllBytes(Paths.get(url.toURI()));
        final byte[] scaled = ImageScaler.downToA4(orig, "jpg");
        assertThat(hasJpgSignature(scaled)).isTrue();
    }

    @Test
    void rotateLandscapeToPortrait() throws Exception {
        URL url = getClass().getResource("/pdf/landscape.jpg");
        byte[] orig = Files.readAllBytes(Paths.get(url.toURI()));
        byte[] scaled = ImageScaler.downToA4(orig, "jpg");
        BufferedImage origImg = fromBytes(orig);
        BufferedImage scaledImg = fromBytes(scaled);
        assertThat(origImg.getWidth()).isGreaterThan(origImg.getHeight());
        assertThat(scaledImg.getHeight()).isGreaterThan(scaledImg.getWidth());
    }

    public boolean hasJpgSignature(byte[] bytes) {
        return (bytes[0] & 0XFF) == 0xFF &&
                (bytes[1] & 0XFF) == 0xD8 &&
                (bytes[0] & 0XFF) == 0xFF;
    }

    private static BufferedImage fromBytes(byte[] bytes) {
        try (InputStream in = new ByteArrayInputStream(bytes)) {
            return ImageIO.read(in);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
