package no.nav.foreldrepenger.selvbetjening.innsyn;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.innsyn.FpSak;
import no.nav.foreldrepenger.common.innsyn.RettighetType;
import no.nav.foreldrepenger.common.innsyn.Saker;
import no.nav.foreldrepenger.common.innsyn.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.http.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.DokumentDto;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.DokumentTjeneste;

class InnsynControllerTest {

    private static final String FAKE_SAKSNUMMER = "123456789";
    private static final Fødselsnummer DUMMY_FNR = new Fødselsnummer("0000");
    private Innsyn innsyn = mock(Innsyn.class);
    private DokumentTjeneste dokumentTjeneste = mock(DokumentTjeneste.class);
    private TokenUtil tokenUtil = mock(TokenUtil.class);

    @BeforeEach
    void setUp() {
        when(tokenUtil.autentisertBrukerOrElseThrowException()).thenReturn(DUMMY_FNR);
    }

    @Test
    void sakErOppdatertHvisOppdateringstidspunktErEtterMottattidspunktetTilJournalposten() {
        var innsynController = new InnsynController(innsyn, dokumentTjeneste, tokenUtil);

        var arkivertSøknadOmForeldrepenger = arkivertSøknadOmForeldrepenger(LocalDateTime.now().minusHours(1), FAKE_SAKSNUMMER);
        var arkiverteDokumenter = List.of(arkivertSøknadOmForeldrepenger);
        when(dokumentTjeneste.alle(any())).thenReturn(arkiverteDokumenter);
        var saker = new Saker(Set.of(fpsak(FAKE_SAKSNUMMER, LocalDateTime.now())), Set.of(), Set.of());
        when(innsyn.hentSaker()).thenReturn(saker);

        assertThat(innsynController.erSakOppdatert()).isTrue();
    }

    @Test
    void sakErIkkeOppdatertHvisOppdatertTidspunktetErFørInnsendingstidspunkt() {
        var innsynController = new InnsynController(innsyn, dokumentTjeneste, tokenUtil);
        var søknadOmForeldrepenger1 = arkivertSøknadOmForeldrepenger(LocalDateTime.now().minusHours(1), FAKE_SAKSNUMMER);
        var søknadOmForeldrepenger2 = arkivertSøknadOmForeldrepenger(LocalDateTime.now(), FAKE_SAKSNUMMER);
        var arkiverteDokumenter = List.of(søknadOmForeldrepenger1, søknadOmForeldrepenger2);

        when(dokumentTjeneste.alle(any())).thenReturn(arkiverteDokumenter);

        var saker = new Saker(Set.of(fpsak(FAKE_SAKSNUMMER, LocalDateTime.now().minusMinutes(30))), Set.of(), Set.of());
        when(innsyn.hentSaker()).thenReturn(saker);

        assertThat(innsynController.erSakOppdatert()).isFalse();
    }

    @Test
    void ingenSakerIFpoversiktMenArkivertSøknadSkalReturnereFalse() {
        var innsynController = new InnsynController(innsyn, dokumentTjeneste, tokenUtil);

        var søknadOmForeldrepenger = arkivertSøknadOmForeldrepenger(LocalDateTime.now(), FAKE_SAKSNUMMER);
        var arkiverteDokumenter = List.of(søknadOmForeldrepenger);
        when(dokumentTjeneste.alle(any())).thenReturn(arkiverteDokumenter);

        var saker = new Saker(Set.of(), Set.of(), Set.of());
        when(innsyn.hentSaker()).thenReturn(saker);

        assertThat(innsynController.erSakOppdatert()).isFalse();
    }

    @Test
    void ingenSakerMedLiktSaksnummerIFpoversiktMenArkivertSøknadSkalReturnereFalse() {
        var innsynController = new InnsynController(innsyn, dokumentTjeneste, tokenUtil);

        var søknadOmForeldrepenger = arkivertSøknadOmForeldrepenger(LocalDateTime.now(), FAKE_SAKSNUMMER);
        var arkiverteDokumenter = List.of(søknadOmForeldrepenger);
        when(dokumentTjeneste.alle(any())).thenReturn(arkiverteDokumenter);

        var saker = new Saker(Set.of(fpsak("987654321", LocalDateTime.now().minusMinutes(30))), Set.of(), Set.of());
        when(innsyn.hentSaker()).thenReturn(saker);

        assertThat(innsynController.erSakOppdatert()).isFalse();
    }

    @Test
    void nårSøknadIkkeHarSaksnummerAntarViAtSakenIkkeErOppdatert() {
        var innsynController = new InnsynController(innsyn, dokumentTjeneste, tokenUtil);

        var søknadOmForeldrepenger1 = arkivertSøknadOmForeldrepenger(LocalDateTime.now(), null);
        var søknadOmForeldrepenger2 = arkivertSøknadOmForeldrepenger(LocalDateTime.now(), FAKE_SAKSNUMMER);
        var arkiverteDokumenter = List.of(søknadOmForeldrepenger1, søknadOmForeldrepenger2);
        when(dokumentTjeneste.alle(any())).thenReturn(arkiverteDokumenter);

        var saker = new Saker(Set.of(fpsak("987654321", LocalDateTime.now().minusMinutes(30))), Set.of(), Set.of());
        when(innsyn.hentSaker()).thenReturn(saker);

        assertThat(innsynController.erSakOppdatert()).isFalse();
    }

    private static DokumentDto arkivertSøknadOmForeldrepenger(LocalDateTime mottatt, String saksnummer) {
        return new DokumentDto(
            "Søknad om foreldrepenger",
            DokumentDto.Type.INNGÅENDE_DOKUMENT,
            saksnummer,
            "12345",
            "123456",
            mottatt);
    }

    private static FpSak fpsak(String saksnummer, LocalDateTime oppdateringstidspunkt) {
        return new FpSak(
            new Saksnummer(saksnummer),
            false,
            true,
            true,
            false,
            false,
            false,
            false,
            RettighetType.BEGGE_RETT,
            null,
            null,
            null,
            null,
            null,
            null,
            oppdateringstidspunkt);
    }
}
