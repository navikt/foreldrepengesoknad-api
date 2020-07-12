package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.unit.DataSize;

import com.google.gson.Gson;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.innsending.pdf.PdfGeneratorStub;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.Attachment;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.Bøtte;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.InMemoryMellomlagring;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.KryptertMellomlagring;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.Mellomlagring;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.MellomlagringKrypto;
import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.vedlegg.PDFEncryptionChecker;
import no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekker;
import no.nav.foreldrepenger.selvbetjening.virusscan.VirusScanner;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class KryptertMellomlagringTest {

    private static final Gson MAPPER = new Gson();
    @Mock
    VirusScanner scanner;
    @Mock
    TokenUtil util;
    @Mock
    Bøtte b1, b2;
    private KryptertMellomlagring km;

    @BeforeEach
    public void beforeEach() {
        lenient().when(b1.isEnabled()).thenReturn(true);
        lenient().when(b2.isEnabled()).thenReturn(true);
        lenient().when(util.autentisertBruker()).thenReturn("01010111111");
        Mellomlagring mellomlagring = new InMemoryMellomlagring(b1, b2);
        km = new KryptertMellomlagring(mellomlagring,
                new MellomlagringKrypto("passphrase", util),
                new VedleggSjekker(DataSize.ofMegabytes(32), DataSize.ofMegabytes(8), scanner,
                        new PDFEncryptionChecker()));
    }

    @Test
    public void TestKryptertSøknad() {
        km.lagreKryptertSøknad("Søknad");
        var lest = km.lesKryptertSøknad();
        assertTrue(lest.isPresent());
        assertEquals(lest.get(), "Søknad");
        km.slettKryptertSøknad();
        lest = km.lesKryptertSøknad();
        assertFalse(km.lesKryptertSøknad().isPresent());
    }

    @Test
    public void TestKryptertKvittering() throws Exception {
        Kvittering kvittering = new Kvittering(LocalDateTime.now(), "33",
                "OK", "id", "42", new byte[0], new byte[0], LocalDate.now(), LocalDate.now());
        km.lagreKryptertKvittering("kvittering", MAPPER.toJson(kvittering));
        var lest = km.lesKryptertKvittering("kvittering");
        assertTrue(lest.isPresent());
        assertEquals(kvittering, MAPPER.fromJson(lest.get(), Kvittering.class));
    }

    @Test
    public void TestKryptertVedlegg() throws Exception {
        var pdf = new PdfGeneratorStub().generate("test");
        Attachment original = Attachment
                .of(new MockMultipartFile("vedlegg", "originalt vedlegg", "application/pdf", pdf));
        km.lagreKryptertVedlegg(original);
        var lest = km.lesKryptertVedlegg(original.getUuid());
        assertTrue(lest.isPresent());
        assertEquals(original, lest.get());
        km.slettKryptertVedlegg(original.getUuid());
        assertFalse(km.lesKryptertVedlegg(original.getUuid()).isPresent());
    }
}
