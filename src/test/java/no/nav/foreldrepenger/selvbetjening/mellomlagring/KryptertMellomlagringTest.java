package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.vedlegg.DelegerendeVedleggSjekker;
import no.nav.foreldrepenger.selvbetjening.vedlegg.PDFEncryptionVedleggSjekker;
import no.nav.foreldrepenger.selvbetjening.vedlegg.StørrelseVedleggSjekker;
import no.nav.foreldrepenger.selvbetjening.vedlegg.virusscan.ClamAvVirusScanner;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class KryptertMellomlagringTest {

    private static final Gson MAPPER = new Gson();
    @Mock
    ClamAvVirusScanner scanner;
    @Mock
    TokenUtil util;
    @Mock
    Bøtte b1, b2;
    private KryptertMellomlagring km;

    @BeforeEach
    void beforeEach() {
        when(util.autentisertBruker()).thenReturn("01010111111");
        var mellomlagring = new InMemoryMellomlagring(b1, b2);
        km = new KryptertMellomlagring(mellomlagring,
                new MellomlagringKrypto("passphrase", util),
                new DelegerendeVedleggSjekker(new StørrelseVedleggSjekker(DataSize.ofMegabytes(32), DataSize.ofMegabytes(8)), scanner,
                        new PDFEncryptionVedleggSjekker()));
    }

    @Test
    void TestKryptertSøknad() {
        when(b2.isEnabled()).thenReturn(true);
        km.lagreKryptertSøknad("Søknad");
        var lest = km.lesKryptertSøknad();
        assertThat(lest).isPresent();
        assertEquals(lest.get(), "Søknad");
        km.slettKryptertSøknad();
        assertThat(km.lesKryptertSøknad()).isNotPresent();
    }

    @Test
    void TestKryptertKvittering() {
        when(b1.isEnabled()).thenReturn(true);
        var kvittering = new Kvittering(LocalDateTime.now(), "42", new byte[0], new byte[0]);
        km.lagreKryptertKvittering("kvittering", MAPPER.toJson(kvittering));
        var lest = km.lesKryptertKvittering("kvittering");
        assertThat(lest).isPresent();
        assertEquals(kvittering, MAPPER.fromJson(lest.get(), Kvittering.class));
    }

    @Test
    void TestKryptertVedlegg() {
        when(b2.isEnabled()).thenReturn(true);
        var pdf = new PdfGeneratorStub().generate("test");
        var original = Attachment.of(new MockMultipartFile("vedlegg", "originalt vedlegg", "application/pdf", pdf));
        km.lagreKryptertVedlegg(original);
        var lest = km.lesKryptertVedlegg(original.getUuid());
        assertThat(lest).isPresent();
        assertEquals(original, lest.get());
        km.slettKryptertVedlegg(original.getUuid());
        assertThat(km.lesKryptertSøknad()).isNotPresent();
    }
}
