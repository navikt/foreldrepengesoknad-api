package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static no.nav.foreldrepenger.selvbetjening.mellomlagring.YtelseMellomlagringType.FORELDREPENGER;
import static no.nav.foreldrepenger.selvbetjening.mellomlagring.YtelseMellomlagringType.SVANGERSKAPSPENGER;
import static no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekkerTest.fraResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import no.nav.foreldrepenger.selvbetjening.http.TokenUtil;

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
        var ytelse = YtelseMellomlagringType.ENGANGSSTONAD;
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
        km.lagreKryptertSøknad("Søknad ES", YtelseMellomlagringType.ENGANGSSTONAD);

        var mellomlagretFP = km.lesKryptertSøknad(FORELDREPENGER);
        var mellomlagretES = km.lesKryptertSøknad(YtelseMellomlagringType.ENGANGSSTONAD);

        assertThat(mellomlagretFP).contains("Søknad FP");
        assertThat(mellomlagretES).contains("Søknad ES");

        km.slettMellomlagring(YtelseMellomlagringType.ENGANGSSTONAD);
        assertThat(km.lesKryptertSøknad(FORELDREPENGER)).isPresent();
        assertThat(km.lesKryptertSøknad(YtelseMellomlagringType.ENGANGSSTONAD)).isNotPresent();
    }

    @Test
    void mellomlagringVedleggLagreLesSlettRoundtripTest() {
        var vedlegg = Attachment.of(fraResource("pdf/junit-test.pdf"));
        km.lagreKryptertVedlegg(vedlegg, FORELDREPENGER);

        var lest = km.lesKryptertVedlegg(vedlegg.uuid(), FORELDREPENGER);
        assertThat(lest).contains(vedlegg.bytes());

        km.slettMellomlagring(FORELDREPENGER);
        assertThat(km.lesKryptertSøknad(FORELDREPENGER)).isNotPresent();
    }
}
