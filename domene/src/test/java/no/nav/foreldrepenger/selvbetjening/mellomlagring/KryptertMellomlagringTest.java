package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static no.nav.foreldrepenger.selvbetjening.mellomlagring.Ytelse.FORELDREPENGER;
import static no.nav.foreldrepenger.selvbetjening.mellomlagring.Ytelse.SVANGERSKAPSPENGER;
import static no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekkerTest.fraResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import no.nav.foreldrepenger.common.util.TokenUtil;

@ExtendWith(MockitoExtension.class)
class KryptertMellomlagringTest {
    @Mock
    TokenUtil util;

    private KryptertMellomlagring km;

    @BeforeEach
    void beforeEach() {
        when(util.getSubject()).thenReturn("01010111111");
        var mellomlagring = new InMemoryMellomlagring();
        var krypto = new MellomlagringKrypto("passphrase", util);
        km = new KryptertMellomlagring(mellomlagring, krypto);
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
    void kryptertForeldrepengeSøknadRoundtripTest() {
        var ytelse = FORELDREPENGER;
        km.lagreKryptertSøknad("Søknad", ytelse);

        var lest = km.lesKryptertSøknad(ytelse);
        assertThat(lest).isPresent();
        assertThat(lest.get()).contains("Søknad");

        km.slettMellomlagring(ytelse);
        assertThat(km.lesKryptertSøknad(ytelse)).isNotPresent();
    }

    @Test
    void TestKryptertSøknadEngangssøknad() {
        var ytelse = Ytelse.ENGANGSSTONAD;
        km.lagreKryptertSøknad("Søknad", ytelse);

        var lest = km.lesKryptertSøknad(ytelse);
        assertThat(lest).isPresent();
        assertThat(lest.get()).contains("Søknad");

        km.slettMellomlagring(ytelse);
        assertThat(km.lesKryptertSøknad(ytelse)).isNotPresent();
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

        km.slettMellomlagring(Ytelse.ENGANGSSTONAD);
        assertThat(km.lesKryptertSøknad(FORELDREPENGER)).isPresent();
        assertThat(km.lesKryptertSøknad(Ytelse.ENGANGSSTONAD)).isNotPresent();
    }

    @Test
    void mellomlagringVedleggLagreLesSlettRoundtripTest() {
        var vedlegg = Attachment.of("filen.pdf", fraResource("pdf/junit-test.pdf"), MediaType.APPLICATION_PDF);
        km.lagreKryptertVedlegg(vedlegg, FORELDREPENGER);

        var lest = km.lesKryptertVedlegg(vedlegg.getUuid(), FORELDREPENGER);
        assertThat(lest).contains(vedlegg.getBytes());

        km.slettMellomlagring(FORELDREPENGER);
        assertThat(km.lesKryptertSøknad(FORELDREPENGER)).isNotPresent();
    }
}
