package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.unit.DataSize;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.innsending.InnsendingConnection;
import no.nav.foreldrepenger.selvbetjening.innsending.InnsendingTjeneste;
import no.nav.foreldrepenger.selvbetjening.innsending.pdf.PdfGenerator;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.engangsstønad.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.Attachment;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.KryptertMellomlagring;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.Ytelse;
import no.nav.foreldrepenger.selvbetjening.vedlegg.virusscan.ClamAvVirusScanner;
import no.nav.foreldrepenger.selvbetjening.vedlegg.virusscan.VirusScanConnection;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = JacksonConfiguration.class)
class VedleggSjekkerTest {

    @Mock
    private InnsendingConnection connection;
    @Mock
    private KryptertMellomlagring mellomlagring;
    @Mock
    private PdfGenerator pdfGenerator;
    @Mock
    private VirusScanConnection virusScanConnection;
    @Autowired
    ObjectMapper mapper;

    private static final StørrelseVedleggSjekker sjekker = new StørrelseVedleggSjekker(DataSize.ofMegabytes(7), DataSize.ofMegabytes(3));


    @Test
    void størrelseVedleggSjekkerhappyCase() {
        // Hvert enkelt eller total overstiger ikke maks
        assertDoesNotThrow(() -> sjekker.sjekk(attachment(2), attachment(2)));
        assertDoesNotThrow(() -> sjekker.sjekk(vedlegg(2), vedlegg(2)));
    }

    @Test
    void størrelseVedleggSjekkerHiverAttachmentTooLargeExceptionVedForStortEnkeltVedlegg() {
        var attachmentOverMaxEnkel = attachment(4);
        var vedleggOverMaxEnkel = vedlegg(4);

        assertThatThrownBy(() -> sjekker.sjekk(attachmentOverMaxEnkel))
            .isInstanceOf(AttachmentTooLargeException.class);
        assertThatThrownBy(() -> sjekker.sjekk(vedleggOverMaxEnkel))
            .isInstanceOf(AttachmentTooLargeException.class);
    }
    @Test
    void størrelseVedleggSjekkerHiverAttachmentsTooLargeExceptionVedForStoreVedleggTotalt() {
        var attachment2MB = attachment(2);
        var vedlegg2MB = vedlegg(2);

        // Totalt 8MB (maks er 7)
        assertThatThrownBy(() ->
            sjekker.sjekk(
                attachment2MB,
                attachment2MB,
                attachment2MB,
                attachment2MB))
            .isInstanceOf(AttachmentsTooLargeException.class);
        assertThatThrownBy(() ->
            sjekker.sjekk(
                vedlegg2MB,
                vedlegg2MB,
                vedlegg2MB,
                vedlegg2MB))
            .isInstanceOf(AttachmentsTooLargeException.class);
    }

    @Test
    void exceptionNårAttachmentIkkeHarInnhold() {
        var attachment0B = attachment(0);
        assertThatThrownBy(() -> sjekker.sjekk(attachment0B)).isInstanceOf(AttachmentUnreadableException.class);
    }

    @Test
    void ClamAvVirusScannerSkalIkkeCatcheExceptions() {
        var vedlegg = vedlegg(1);
        var attachment = attachment(1);
        doThrow(AttachmentVirusException.class).when(virusScanConnection).scan(any(), any());
        var clamAvVirusScanner = new ClamAvVirusScanner(virusScanConnection);


        assertThatThrownBy(() -> clamAvVirusScanner.sjekk(vedlegg)).isInstanceOf(AttachmentVirusException.class);
        assertThatThrownBy(() -> clamAvVirusScanner.sjekk(attachment)).isInstanceOf(AttachmentVirusException.class);
    }

    @Test
    void detekterStøttetOgIkkeStøttetFormat() {
        var formatSjekker = new StøttetFormatSjekker();

        var ikkeStøttetOctetStreamFil = vedlegg(1);
        assertThatThrownBy(() -> formatSjekker.sjekk(ikkeStøttetOctetStreamFil)).isInstanceOf(AttachmentTypeUnsupportedException.class);

        var støttedeFormaterListe = List.of(
            fraResource("pdf/landscape.jpg"),
            fraResource("pdf/nav-logo.png"),
            fraResource("pdf/junit-test.pdf"));
        støttedeFormaterListe.stream()
             .map(bytes -> Attachment.of("filnavn", bytes, MediaType.IMAGE_JPEG))
             .forEach(attachment -> assertDoesNotThrow(() -> formatSjekker.sjekk(attachment)));
    }

    @Test
    void verifiserAtInnsendingtjenesteHenterVedlegg() {
        var vedlegg = vedlegg(1);
        var vedleggene = List.of(vedlegg);

        var innsendingTjeneste = new InnsendingTjeneste(connection, mellomlagring, pdfGenerator, mapper);
        when(mellomlagring.lesKryptertVedlegg(vedlegg.getUuid(), Ytelse.ENGANGSSTONAD)).thenReturn(Optional.of(attachment(1)));

        var innsending = new EngangsstønadDto(null, null, null, null, vedleggene);
        innsendingTjeneste.hentMellomlagredeFiler(innsending);

        var nyVedleggHeltLik = vedlegg(1);
        assertThat(vedleggene).hasSize(1);
        assertThat(vedleggene.get(0).getContent()).isEqualTo(nyVedleggHeltLik.getContent());
        assertThat(vedleggene.get(0).getBeskrivelse()).isEqualTo(nyVedleggHeltLik.getBeskrivelse());
        assertThat(vedleggene.get(0).getId()).isEqualTo(nyVedleggHeltLik.getId());
        assertThat(vedleggene.get(0).getInnsendingsType()).isEqualTo(nyVedleggHeltLik.getInnsendingsType()).isNull();
        assertThat(vedleggene.get(0).getSkjemanummer()).isEqualTo(nyVedleggHeltLik.getSkjemanummer());
        assertThat(vedleggene.get(0).getUuid()).isEqualTo(nyVedleggHeltLik.getUuid());
        assertThat(vedleggene.get(0).getUrl()).isEqualTo(nyVedleggHeltLik.getUrl());
    }

    private static Attachment attachment(int megabytes) {
        var content = new byte[((int) DataSize.ofMegabytes(megabytes).toBytes())];
        return Attachment.of("en pdf", content, MediaType.APPLICATION_PDF);
    }

    private static VedleggDto vedlegg(int megabytes) {
        var uuid = "802e2ce7-8106-46cf-afdb-2aecc2b6de7c";
        var content = new byte[((int) DataSize.ofMegabytes(megabytes).toBytes())];
        return new VedleggDto(content, "En stoooor pdf!", new MutableVedleggReferanseDto("V00001"), null, "I000038", uuid, URI.create("https://foreldrepengesoknad-api.intern.dev.nav.no/" + uuid),
            null);
    }

    public static byte[] fraResource(String classPathResource) {
        try (var is = new ClassPathResource(classPathResource).getInputStream()) {
            return is.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
