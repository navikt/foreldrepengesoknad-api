package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.util.StreamUtils.copyToByteArray;
import static org.springframework.util.unit.DataUnit.MEGABYTES;

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

import no.nav.foreldrepenger.selvbetjening.error.AttachmentPasswordProtectedException;
import no.nav.foreldrepenger.selvbetjening.error.AttachmentVirusException;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan.VirusScanner;

@ExtendWith(SpringExtension.class)
public class VedleggSjekkerTest {

    private static final DataSize MAX_SAMLET = DataSize.of(32, MEGABYTES);
    private static final DataSize MAX_ENKELT = DataSize.of(8, MEGABYTES);

    @Mock
    VirusScanner scanner;

    @Mock
    PDFEncryptionChecker encryptionSjekker;

    private VedleggSjekker sjekker;

    @BeforeEach
    public void beforeAll() {
        sjekker = new VedleggSjekker(MAX_SAMLET, MAX_ENKELT, scanner, encryptionSjekker);
    }

    @Test
    public void testSjekkEncrypted() {
        Vedlegg v = vedleggFra("pdf/pdf-with-user-password.pdf");
        doThrow(AttachmentPasswordProtectedException.class).when(encryptionSjekker).checkEncrypted(eq(v));
        assertThrows(AttachmentPasswordProtectedException.class, () -> sjekker.sjekk(v));
        verify(scanner).scan(eq(v));
    }

    @Test
    public void testSjekkUnencrypted() {
        Vedlegg v = vedleggFra("pdf/pdf-with-empty-user-password.pdf");
        sjekker.sjekk(v);
        verify(scanner).scan(eq(v));
    }

    @Test
    public void testImage() {
        Vedlegg v = vedleggFra("pdf/nav-logo.png");
        sjekker.sjekk(v);
        verify(scanner).scan(eq(v));
    }

    @Test
    public void testVirus() {
        Vedlegg v = vedleggFra("pdf/pdf-with-empty-user-password.pdf");
        doThrow(AttachmentVirusException.class).when(scanner).scan(eq(v));
        assertThrows(AttachmentVirusException.class, () -> sjekker.sjekk(v));
        verify(scanner).scan(eq(v));
    }

    private static Vedlegg vedleggFra(String navn) {
        Vedlegg v = new Vedlegg();
        v.setContent(bytesFra(navn));
        v.setUrl(URI.create(navn));
        return v;
    }

    private static byte[] bytesFra(String filename) {
        try (InputStream is = new ClassPathResource(filename).getInputStream()) {
            return copyToByteArray(is);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}