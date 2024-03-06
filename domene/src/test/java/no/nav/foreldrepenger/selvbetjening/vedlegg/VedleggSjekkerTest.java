package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.unit.DataSize;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.Attachment;
import no.nav.foreldrepenger.selvbetjening.vedlegg.virusscan.ClamAvVirusScanner;
import no.nav.foreldrepenger.selvbetjening.vedlegg.virusscan.VirusScanConnection;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = JacksonConfiguration.class)
public class VedleggSjekkerTest {

    @Mock
    private VirusScanConnection virusScanConnection;
    @Autowired
    ObjectMapper mapper;

    private static final StørrelseVedleggSjekker sjekker = new StørrelseVedleggSjekker(DataSize.ofMegabytes(7), DataSize.ofMegabytes(3));


    @Test
    void størrelseVedleggSjekkerhappyCase() {
        // Hvert enkelt eller total overstiger ikke maks
        assertDoesNotThrow(() -> sjekker.sjekk(attachment(2), attachment(2)));
    }

    @Test
    void størrelseVedleggSjekkerHiverAttachmentTooLargeExceptionVedForStortEnkeltVedlegg() {
        var attachmentOverMaxEnkel = attachment(4);

        assertThatThrownBy(() -> sjekker.sjekk(attachmentOverMaxEnkel))
            .isInstanceOf(AttachmentTooLargeException.class);
    }
    @Test
    void størrelseVedleggSjekkerHiverAttachmentsTooLargeExceptionVedForStoreVedleggTotalt() {
        var attachment2MB = attachment(2);
        // Totalt 8MB (maks er 7)
        assertThatThrownBy(() ->
            sjekker.sjekk(
                attachment2MB,
                attachment2MB,
                attachment2MB,
                attachment2MB))
            .isInstanceOf(AttachmentsTooLargeException.class);
    }

    @Test
    void exceptionNårAttachmentIkkeHarInnhold() {
        var attachment0B = attachment(0);
        assertThatThrownBy(() -> sjekker.sjekk(attachment0B)).isInstanceOf(AttachmentUnreadableException.class);
    }

    @Test
    void ClamAvVirusScannerSkalIkkeCatcheExceptions() {
        var attachment = attachment(1);
        doThrow(AttachmentVirusException.class).when(virusScanConnection).scan(any(), any());
        var clamAvVirusScanner = new ClamAvVirusScanner(virusScanConnection);

        assertThatThrownBy(() -> clamAvVirusScanner.sjekk(attachment)).isInstanceOf(AttachmentVirusException.class);
    }

    @Test
    void detekterStøttetOgIkkeStøttetFormat() {
        var formatSjekker = new StøttetFormatSjekker();
        var støttedeFormaterListe = List.of(
            fraResource("pdf/landscape.jpg"),
            fraResource("pdf/nav-logo.png"),
            fraResource("pdf/junit-test.pdf"));
        støttedeFormaterListe.stream()
             .map(Attachment::of)
             .forEach(attachment -> assertDoesNotThrow(() -> formatSjekker.sjekk(attachment)));
    }

    private static Attachment attachment(int megabytes) {
        var content = megabytes(megabytes);
        return Attachment.of(content);
    }

    private static byte[] megabytes(int megabytes) {
        return new byte[((int) DataSize.ofMegabytes(megabytes).toBytes())];
    }

    public static byte[] fraResource(String classPathResource) {
        try (var is = new ClassPathResource(classPathResource).getInputStream()) {
            return is.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
