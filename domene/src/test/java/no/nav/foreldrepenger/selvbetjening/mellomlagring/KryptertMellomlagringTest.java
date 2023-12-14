package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static no.nav.foreldrepenger.selvbetjening.mellomlagring.Ytelse.FORELDREPENGER;
import static no.nav.foreldrepenger.selvbetjening.mellomlagring.Ytelse.IKKE_OPPGITT;
import static no.nav.foreldrepenger.selvbetjening.mellomlagring.Ytelse.SVANGERSKAPSPENGER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.unit.DataSize;

import com.google.gson.Gson;

import no.nav.foreldrepenger.common.error.UnexpectedInputException;
import no.nav.foreldrepenger.common.util.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.vedlegg.DelegerendeVedleggSjekker;
import no.nav.foreldrepenger.selvbetjening.vedlegg.PDFEncryptionVedleggSjekker;
import no.nav.foreldrepenger.selvbetjening.vedlegg.StørrelseVedleggSjekker;
import no.nav.foreldrepenger.selvbetjening.vedlegg.virusscan.ClamAvVirusScanner;

@ExtendWith(MockitoExtension.class)
class KryptertMellomlagringTest {

    @Mock
    ClamAvVirusScanner scanner;
    @Mock
    TokenUtil util;
    private KryptertMellomlagring km;
    private InMemoryMellomlagring mellomlagring;
    private MellomlagringKrypto krypto;

    @BeforeEach
    void beforeEach() {
        when(util.getSubject()).thenReturn("01010111111");
        mellomlagring = new InMemoryMellomlagring();
        krypto = new MellomlagringKrypto("passphrase", util);
        km = new KryptertMellomlagring(mellomlagring, krypto,
                new DelegerendeVedleggSjekker(new StørrelseVedleggSjekker(DataSize.ofMegabytes(32), DataSize.ofMegabytes(8)), scanner,
                        new PDFEncryptionVedleggSjekker()));
    }

    @Test
    void finnesAktivMellomlagringPåForForeldrepengerPåBruker() {
        km.lagreKryptertSøknad("Søknad", FORELDREPENGER);

        var aktivMellomlagring = km.finnesAktivMellomlagring();

        assertThat(aktivMellomlagring.engangsstonad()).isFalse();
        assertThat(aktivMellomlagring.foreldrepenger()).isTrue();
        assertThat(aktivMellomlagring.svangerskapspenger()).isFalse();
    }

    @Test
    void finnesAktivMellomlagringPåFlereYtelserPåBruker() {
        km.lagreKryptertSøknad("Søknad foreldrepenger", FORELDREPENGER);
        km.lagreKryptertSøknad("Søknad svangerskapspenger", SVANGERSKAPSPENGER);

        var aktivMellomlagring = km.finnesAktivMellomlagring();

        assertThat(aktivMellomlagring.engangsstonad()).isFalse();
        assertThat(aktivMellomlagring.foreldrepenger()).isTrue();
        assertThat(aktivMellomlagring.svangerskapspenger()).isTrue();
    }

    @Test
    void finnesIkkeAktivMellomlagringPåBruker() {
        var aktivMellomlagring = km.finnesAktivMellomlagring();

        assertThat(aktivMellomlagring.engangsstonad()).isFalse();
        assertThat(aktivMellomlagring.foreldrepenger()).isFalse();
        assertThat(aktivMellomlagring.svangerskapspenger()).isFalse();
    }

    @Test
    void TestKryptertSøknad() {
        var ytelse = Ytelse.IKKE_OPPGITT;
        km.lagreKryptertSøknad("Søknad", ytelse);
        var lest = km.lesKryptertSøknad(ytelse);
        assertThat(lest).isPresent();
        assertThat(lest.get()).contains("Søknad");
        km.slettKryptertSøknad(ytelse);
        assertThat(km.lesKryptertSøknad(ytelse)).isNotPresent();
    }

    @Test
    void TestKryptertSøknadEngangssøknad() {
        var ytelse = Ytelse.ENGANGSSTONAD;
        km.lagreKryptertSøknad("Søknad", ytelse);
        var lest = km.lesKryptertSøknad(ytelse);
        assertThat(lest).isPresent();
        assertThat(lest.get()).contains("Søknad");
        km.slettKryptertSøknad(ytelse);
        assertThat(km.lesKryptertSøknad(ytelse)).isNotPresent();
    }

    @Test
    void mellomlagretSøknadPåGammelFormatSkalErtattesMedNyttFormat() {
        mellomlagring.lagre(krypto.mappenavn(), FORELDREPENGER.name(), krypto.encrypt("Søknad verdi"), false);

        var lest1 = km.lesKryptertSøknad(FORELDREPENGER);
        assertThat(lest1).isPresent();
        assertThat(lest1.get()).contains("Søknad");

        var lest2 = km.lesKryptertSøknad(FORELDREPENGER);
        assertThat(lest2).isPresent();
        assertThat(lest2.get()).isEqualTo(lest1.get());

        km.slettKryptertSøknad(FORELDREPENGER);
        assertThat(km.lesKryptertSøknad(FORELDREPENGER)).isNotPresent();
    }

    @Test
    void slettingAvMellomlagretESSkalIkkeSletteMellomlagretFP() {
        km.lagreKryptertSøknad("Søknad FP", FORELDREPENGER);
        km.lagreKryptertSøknad("Søknad ES", Ytelse.ENGANGSSTONAD);

        var mellomlagretFP = km.lesKryptertSøknad(FORELDREPENGER);
        var mellomlagretES = km.lesKryptertSøknad(Ytelse.ENGANGSSTONAD);

        assertThat(mellomlagretFP).isPresent();
        assertThat(mellomlagretFP.get()).isEqualTo("Søknad FP");
        assertThat(mellomlagretES).isPresent();
        assertThat(mellomlagretES.get()).isEqualTo("Søknad ES");

        km.slettKryptertSøknad(Ytelse.ENGANGSSTONAD);
        assertThat(km.lesKryptertSøknad(FORELDREPENGER)).isPresent();
        assertThat(km.lesKryptertSøknad(Ytelse.ENGANGSSTONAD)).isNotPresent();
    }

    @Test
    void mellomlagretVedleggPåGammelFormatSkalErtattesMedNyttFormat() {
        var vedlegg = Attachment.of("filen.pdf", new byte[]{1,2,3,4,5,6,7,8}, MediaType.APPLICATION_PDF);
        mellomlagring.lagre(krypto.mappenavn(), vedlegg.getUuid(), krypto.encrypt(new Gson().toJson(vedlegg)), false); // Lagrer på gammelt format

        var lest1 = km.lesKryptertVedlegg(vedlegg.getUuid(), FORELDREPENGER);
        assertThat(lest1).contains(vedlegg);

        var lest2 =  km.lesKryptertVedlegg(vedlegg.getUuid(), FORELDREPENGER);
        assertThat(lest2).contains(lest1.get());

        km.slettKryptertSøknad(FORELDREPENGER);
        assertThat(km.lesKryptertSøknad(FORELDREPENGER)).isNotPresent();
    }


    @Test
    void mellomlagringAvVedleddSkalSletteGamleFilerOgOpprettePåNyttFormat() {
        var vedlegg = Attachment.of("filen.pdf", new byte[]{1,2,3,4,5,6,7,8}, MediaType.APPLICATION_PDF);
        mellomlagring.lagre(krypto.mappenavn(), vedlegg.getUuid(), krypto.encrypt(new Gson().toJson(vedlegg)), false); // Lagrer på gammelt format

        var lest1 = km.lesKryptertVedlegg(vedlegg.getUuid(), IKKE_OPPGITT);
        assertThat(lest1).contains(vedlegg);

        var lest2 =  km.lesKryptertVedlegg(vedlegg.getUuid(), FORELDREPENGER);
        assertThat(lest2).contains(lest1.get());

        km.slettKryptertSøknad(FORELDREPENGER);
        assertThat(km.lesKryptertSøknad(FORELDREPENGER)).isNotPresent();
        assertThat(km.lesKryptertSøknad(IKKE_OPPGITT)).isNotPresent();
    }


    @Test
    void mellomlagringVedleggLagreLesSlettRoundtripTest() {
        var vedlegg = Attachment.of("filen.pdf", new byte[]{1,2,3,4,5,6,7,8}, MediaType.APPLICATION_PDF);
        km.lagreKryptertVedlegg(vedlegg, FORELDREPENGER);

        var lest = km.lesKryptertVedlegg(vedlegg.getUuid(), FORELDREPENGER);
        assertThat(lest).contains(vedlegg);

        km.slettKryptertSøknad(FORELDREPENGER);
        assertThat(km.lesKryptertSøknad(FORELDREPENGER)).isNotPresent();
    }

    @Test
    void TestKryptertVedlegg() {
        var pdf = generatePdf();
        var original = Attachment.of(new MockMultipartFile("vedlegg", "originalt vedlegg", "application/pdf", pdf));
        km.lagreKryptertVedlegg(original, Ytelse.IKKE_OPPGITT);
        var lest = km.lesKryptertVedlegg(original.getUuid(), Ytelse.IKKE_OPPGITT);
        assertThat(lest).isPresent();
        assertEquals(original, lest.get());
        km.slettKryptertVedlegg(original.getUuid(), Ytelse.IKKE_OPPGITT);
        assertThat(km.lesKryptertSøknad(Ytelse.IKKE_OPPGITT)).isNotPresent();
    }

    private static byte[] generatePdf() {

        try (var document = new PDDocument(); var baos = new ByteArrayOutputStream()) {

            var page = new PDPage();
            document.addPage(page);

            var contentStream = new PDPageContentStream(document, page);

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER), 12);
            contentStream.beginText();
            contentStream.showText("test");
            contentStream.endText();
            contentStream.close();
            document.save(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new UnexpectedInputException("Kunne ikke lage PDF", e);
        }
    }
}
