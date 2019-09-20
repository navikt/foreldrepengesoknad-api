package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.util.StreamUtils.copyToByteArray;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import no.nav.foreldrepenger.selvbetjening.error.AttachmentPasswordProtectedException;
import no.nav.foreldrepenger.selvbetjening.error.AttachmentVirusException;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan.VirusScanner;

@ExtendWith(SpringExtension.class)
public class VedleggSjekkerTest {

    private static final DataSize max = DataSize.of(32, DataUnit.MEGABYTES);
    private static final DataSize maxEnkelt = DataSize.of(8, DataUnit.MEGABYTES);

    @Mock
    VirusScanner scanner;

    @Mock
    PDFEncryptionChecker encryptionSjekker;

    private VedleggSjekker sjekker;

    @BeforeEach
    public void beforeAll() {
        sjekker = new VedleggSjekker(max, maxEnkelt, scanner, encryptionSjekker);
    }

    @Test
    public void testSjekkEncrypted() {
        Vedlegg v = new Vedlegg();
        v.setContent(bytesFra("pdf/pdf-with-user-password.pdf"));
        v.setUrl(URI.create("pdf/pdf-with-user-password.pdf"));
        doThrow(AttachmentPasswordProtectedException.class).when(encryptionSjekker).checkEncrypted(eq(v));
        assertThrows(AttachmentPasswordProtectedException.class, () -> sjekker.sjekk(v));
        verify(scanner).scan(eq(v));
    }

    @Test
    public void testSjekkUnencrypted() {
        Vedlegg v = new Vedlegg();
        v.setContent(bytesFra("pdf/pdf-with-empty-user-password.pdf"));
        v.setUrl(URI.create("pdf/pdf-with-empty-user-password.pdf"));
        sjekker.sjekk(v);
        verify(scanner).scan(eq(v));
    }

    @Test
    public void testImage() {
        Vedlegg v = new Vedlegg();
        v.setContent(bytesFra("pdf/nav-logo.png"));
        v.setUrl(URI.create("pdf/nav-logo.png"));
        sjekker.sjekk(v);
        verify(scanner).scan(eq(v));
    }

    @Test
    public void testVirus() {
        Vedlegg v = new Vedlegg();
        v.setContent(bytesFra("pdf/pdf-with-empty-user-password.pdf"));
        v.setUrl(URI.create("pdf/pdf-with-empty-user-password.pdf"));
        doThrow(AttachmentVirusException.class).when(scanner).scan(eq(v));
        assertThrows(AttachmentVirusException.class, () -> sjekker.sjekk(v));
        verify(scanner).scan(eq(v));
    }

    private static byte[] bytesFra(String filename) {
        try (InputStream is = new ClassPathResource(filename).getInputStream()) {
            return copyToByteArray(is);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}