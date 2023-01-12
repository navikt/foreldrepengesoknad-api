package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.unit.DataSize;

import no.nav.foreldrepenger.selvbetjening.innsending.InnsendingConnection;
import no.nav.foreldrepenger.selvbetjening.innsending.InnsendingTjeneste;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.VedleggFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.pdf.PdfGenerator;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.Attachment;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.KryptertMellomlagring;
import no.nav.foreldrepenger.selvbetjening.vedlegg.virusscan.ClamAvVirusScanner;
import no.nav.foreldrepenger.selvbetjening.vedlegg.virusscan.VirusScanConnection;

@ExtendWith(MockitoExtension.class)
class VedleggSjekkerTest {

    @Mock
    private InnsendingConnection connection;
    @Mock
    private KryptertMellomlagring mellomlagring;
    @Mock
    private PdfGenerator pdfGenerator;
    @Mock
    private VirusScanConnection virusScanConnection;


    @Test
    void størrelseVedleggSjekkerhappyCase() {
        var maxTotal = DataSize.ofMegabytes(7);
        var maxEnkel = DataSize.ofMegabytes(3);
        var størrelseVedleggSjekker = new StørrelseVedleggSjekker(maxTotal, maxEnkel);

        // Hvert enkelt eller total overstiger ikke maks
        størrelseVedleggSjekker.sjekk(attachment(2), attachment(2));
        størrelseVedleggSjekker.sjekk(vedlegg(2), vedlegg(2));
    }

    @Test
    void størrelseVedleggSjekkerHiverAttachmentTooLargeExceptionVedForStortEnkeltVedlegg() {
        var maxTotal = DataSize.ofMegabytes(7);
        var maxEnkel = DataSize.ofMegabytes(3);
        var størrelseVedleggSjekker = new StørrelseVedleggSjekker(maxTotal, maxEnkel);

        var attachmentOverMaxEnkel = attachment(4);
        var vedleggOverMaxEnkel = vedlegg(4);

        assertThatThrownBy(() -> størrelseVedleggSjekker.sjekk(attachmentOverMaxEnkel))
            .isInstanceOf(AttachmentTooLargeException.class);
        assertThatThrownBy(() -> størrelseVedleggSjekker.sjekk(vedleggOverMaxEnkel))
            .isInstanceOf(AttachmentTooLargeException.class);
    }
    @Test
    void størrelseVedleggSjekkerHiverAttachmentsTooLargeExceptionVedForStoreVedleggTotalt() {
        var maxTotal = DataSize.ofMegabytes(7);
        var maxEnkel = DataSize.ofMegabytes(3);
        var størrelseVedleggSjekker = new StørrelseVedleggSjekker(maxTotal, maxEnkel);

        var attachment2MB = attachment(2);
        var vedlegg2MB = vedlegg(2);

        // Totalt 8MB (maks er 7)
        assertThatThrownBy(() ->
            størrelseVedleggSjekker.sjekk(
                attachment2MB,
                attachment2MB,
                attachment2MB,
                attachment2MB))
            .isInstanceOf(AttachmentsTooLargeException.class);
        assertThatThrownBy(() ->
            størrelseVedleggSjekker.sjekk(
                vedlegg2MB,
                vedlegg2MB,
                vedlegg2MB,
                vedlegg2MB))
            .isInstanceOf(AttachmentsTooLargeException.class);
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
    void verifiserAtVedleggSjekkeneIkkeModifisererInnholdetUtenomÅLeggeTilContent() {
        var vedlegg = vedlegg(4);
        var vedleggene = List.of(vedlegg);
        var delegerendeVedleggSjekker = new DelegerendeVedleggSjekker(
            new StørrelseVedleggSjekker(DataSize.ofMegabytes(64), DataSize.ofMegabytes(16)),
            new PDFEncryptionVedleggSjekker(),
            new ClamAvVirusScanner(virusScanConnection)
        );
        var innsendingTjeneste = new InnsendingTjeneste(connection, mellomlagring, delegerendeVedleggSjekker, pdfGenerator);
        when(mellomlagring.lesKryptertVedlegg(vedlegg.getUuid())).thenReturn(Optional.of(attachment(4)));

        innsendingTjeneste.hentKryptertVedleggOgSjekkerVedlegg(vedleggene);

        var nyVedleggHeltLik = vedlegg(4);
        assertThat(vedleggene).hasSize(1);
        assertThat(vedleggene.get(0).getContent()).isEqualTo(nyVedleggHeltLik.getContent());
        assertThat(vedleggene.get(0).getBeskrivelse()).isEqualTo(nyVedleggHeltLik.getBeskrivelse());
        assertThat(vedleggene.get(0).getId()).isEqualTo(nyVedleggHeltLik.getId());
        assertThat(vedleggene.get(0).getInnsendingsType()).isEqualTo(nyVedleggHeltLik.getInnsendingsType()).isNull();
        assertThat(vedleggene.get(0).getSkjemanummer()).isEqualTo(nyVedleggHeltLik.getSkjemanummer());
        assertThat(vedleggene.get(0).getUuid()).isEqualTo(nyVedleggHeltLik.getUuid());
        assertThat(vedleggene.get(0).getUrl()).isEqualTo(nyVedleggHeltLik.getUrl());
    }

    @Test
    void ikkeStøttetFormat() throws IOException {
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

    private static Attachment attachment(int megabytes) {
        var content = new byte[((int) DataSize.ofMegabytes(megabytes).toBytes())];
        return Attachment.of("en pdf", content, MediaType.APPLICATION_PDF);
    }

    private static VedleggFrontend vedlegg(int megabytes) {
        var uuid = "802e2ce7-8106-46cf-afdb-2aecc2b6de7c";
        var content = new byte[((int) DataSize.ofMegabytes(megabytes).toBytes())];
        return new VedleggFrontend(content, "En stoooor pdf!", "V00001", null, "I000038", uuid, URI.create("https://foreldrepengesoknad-api.nav.no/" + uuid));
    }

    private static byte[] fraResource(String classPathResource) throws IOException {
        return new ClassPathResource(classPathResource).getInputStream().readAllBytes();
    }
}
