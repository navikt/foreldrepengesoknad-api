package no.nav.foreldrepenger.selvbetjening.felles.attachments;

import org.junit.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImageScalerTest {

    @Test
    public void imgSizeHasChanged() throws Exception {
        URL url = getClass().getResource("/pdf/jks.jpg");
        byte[] orig = Files.readAllBytes(Paths.get(url.toURI()));
        byte[] scaled = ImageScaler.toA4(orig, "jpg");
        assertTrue(scaled.length != orig.length);
    }

    @Test
    public void scaledImgHasRetainedMagicBytes() throws Exception {
        URL url = getClass().getResource("/pdf/jks.jpg");
        final byte[] orig = Files.readAllBytes(Paths.get(url.toURI()));
        final byte[] scaled = ImageScaler.toA4(orig, "jpg");
        assertTrue(hasJpgSignature(scaled));
    }

    public static boolean hasJpgSignature(byte[] bytes) {
        return (bytes[0] & 0XFF) == 0xFF &&
                (bytes[1] & 0XFF) == 0xD8 &&
                (bytes[0] & 0XFF) == 0xFF;
    }

}
