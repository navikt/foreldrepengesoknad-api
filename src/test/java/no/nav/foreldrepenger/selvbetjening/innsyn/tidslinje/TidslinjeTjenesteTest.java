package no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.innsyn.InntektsmeldingDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.DokumentTypeId;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.EnkelJournalpost;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.SafSelvbetjeningTjeneste;
import no.nav.foreldrepenger.selvbetjening.innsyn.Innsyn;


public class TidslinjeTjenesteTest {
    private static final Saksnummer DUMMY_SAKSNUMMER = new Saksnummer("0000000");
    private static final Fødselsnummer DUMMY_FNR = new Fødselsnummer("0000000");

    private TidslinjeTjeneste tjeneste;
    private SafSelvbetjeningTjeneste safselvbetjeningTjeneste;
    private Innsyn innsyn;

    @BeforeEach
    void setUp() {
        safselvbetjeningTjeneste = mock(SafSelvbetjeningTjeneste.class);
        innsyn = mock(Innsyn.class);
        tjeneste = new TidslinjeTjeneste(safselvbetjeningTjeneste, innsyn);
    }

    @Test
    void søknadOgInntektmeldingTidslije() {
        var saksnummer = DUMMY_SAKSNUMMER;
        var søknadMedVedlegg = søknadMed1Vedlegg(saksnummer, LocalDateTime.now());
        var inntektsmelding = standardInntektsmelding(LocalDateTime.now());
        when(safselvbetjeningTjeneste.alle(DUMMY_FNR, saksnummer)).thenReturn(List.of(søknadMedVedlegg));
        when(innsyn.inntektsmeldinger(saksnummer)).thenReturn(List.of(inntektsmelding));

        var tidslinje = tjeneste.tidslinje(DUMMY_FNR, saksnummer);

        assertThat(tidslinje)
            .hasSize(2)
            .extracting(TidslinjeHendelseDto::tidslinjeHendelseType)
            .containsExactly(
                TidslinjeHendelseDto.TidslinjeHendelseType.FØRSTEGANGSSØKNAD,
                TidslinjeHendelseDto.TidslinjeHendelseType.INNTEKTSMELDING
            );
    }

    @Test
    void søknadEtterlysIMOgVedtak() {
        var saksnummer = DUMMY_SAKSNUMMER;
        var søknadMedVedlegg = søknadMed1Vedlegg(saksnummer, LocalDateTime.now());
        var etterlysIM = etterlysIM(saksnummer);
        var vedtak = utgåendeVedtak(saksnummer, LocalDateTime.now());
        when(safselvbetjeningTjeneste.alle(DUMMY_FNR, saksnummer)).thenReturn(
            List.of(søknadMedVedlegg, etterlysIM, vedtak));

        var tidslinje = tjeneste.tidslinje(DUMMY_FNR, saksnummer);

        assertThat(tidslinje)
            .hasSize(3)
            .extracting(TidslinjeHendelseDto::tidslinjeHendelseType)
            .containsExactly(
                TidslinjeHendelseDto.TidslinjeHendelseType.FØRSTEGANGSSØKNAD,
                TidslinjeHendelseDto.TidslinjeHendelseType.UTGÅENDE_ETTERLYS_INNTEKTSMELDING,
                TidslinjeHendelseDto.TidslinjeHendelseType.VEDTAK
            );
    }

    @Test
    void søknadMedFritekstVedtak() {
        var saksnummer = DUMMY_SAKSNUMMER;
        var utgåendeVedtakFritekts = utgåendeVedtakFritekts(saksnummer, LocalDateTime.now());
        when(safselvbetjeningTjeneste.alle(DUMMY_FNR, saksnummer)).thenReturn(
            List.of(utgåendeVedtakFritekts));

        var tidslinje = tjeneste.tidslinje(DUMMY_FNR, saksnummer);

        assertThat(tidslinje)
            .hasSize(1)
            .extracting(TidslinjeHendelseDto::tidslinjeHendelseType)
            .containsExactly(
                TidslinjeHendelseDto.TidslinjeHendelseType.VEDTAK
            );
    }

    @Test
    void søknadIMEttersendingVedtakEndringssøknadOgDeretterNyttVedtak() {
        var saksnummer = DUMMY_SAKSNUMMER;
        var tidspunkt = LocalDateTime.now();
        var søknadMedVedlegg = søknadMed1Vedlegg(saksnummer, tidspunkt);
        var inntektsmelding = standardInntektsmelding(tidspunkt.plusDays(1));
        var innhentOpplysningsBrev = innhentOpplysningsBrev(saksnummer, tidspunkt.plusDays(2));
        var ettersending = ettersender2Vedlegg(saksnummer, tidspunkt.plusDays(3));
        var vedtak = utgåendeVedtak(saksnummer, tidspunkt.plusDays(4));
        var endringssøknad = endringssøknadUtenVedlegg(saksnummer, tidspunkt.plusDays(4));
        var vedtakEndring = utgåendeVedtak(saksnummer, tidspunkt.plusDays(5));
        when(safselvbetjeningTjeneste.alle(DUMMY_FNR, saksnummer)).thenReturn(
            List.of(søknadMedVedlegg, innhentOpplysningsBrev, ettersending, vedtak, endringssøknad, vedtakEndring));
        when(innsyn.inntektsmeldinger(saksnummer)).thenReturn(List.of(inntektsmelding));

        var tidslinje = tjeneste.tidslinje(DUMMY_FNR, saksnummer);

        assertThat(tidslinje)
            .hasSize(7)
            .extracting(TidslinjeHendelseDto::tidslinjeHendelseType)
            .containsExactly(
                TidslinjeHendelseDto.TidslinjeHendelseType.FØRSTEGANGSSØKNAD,
                TidslinjeHendelseDto.TidslinjeHendelseType.INNTEKTSMELDING,
                TidslinjeHendelseDto.TidslinjeHendelseType.UTGÅENDE_INNHENT_OPPLYSNINGER,
                TidslinjeHendelseDto.TidslinjeHendelseType.ETTERSENDING,
                TidslinjeHendelseDto.TidslinjeHendelseType.VEDTAK,
                TidslinjeHendelseDto.TidslinjeHendelseType.ENDRINGSSØKNAD,
                TidslinjeHendelseDto.TidslinjeHendelseType.VEDTAK
            );
    }

    @Test
    void nyeSøknaderFørVedtakSkalFåTypeFørstegangssøknadNY() {
        var saksnummer = DUMMY_SAKSNUMMER;
        var tidspunkt = LocalDateTime.now();
        var søknadMedVedlegg = søknadMed1Vedlegg(saksnummer, tidspunkt);
        var inntektsmelding = standardInntektsmelding(tidspunkt.plusDays(1));
        var ettersending = ettersender2Vedlegg(saksnummer, tidspunkt.plusDays(2));
        var nySøknadMedVedlegg = søknadMed1Vedlegg(saksnummer, tidspunkt.plusDays(3));
        var vedtak = utgåendeVedtak(saksnummer, tidspunkt.plusDays(4));
        when(safselvbetjeningTjeneste.alle(DUMMY_FNR, saksnummer)).thenReturn(List.of(søknadMedVedlegg, ettersending, nySøknadMedVedlegg, vedtak));
        when(innsyn.inntektsmeldinger(saksnummer)).thenReturn(List.of(inntektsmelding));

        var tidslinje = tjeneste.tidslinje(DUMMY_FNR, saksnummer);

        assertThat(tidslinje)
            .hasSize(5)
            .extracting(TidslinjeHendelseDto::tidslinjeHendelseType)
            .containsExactly(
                TidslinjeHendelseDto.TidslinjeHendelseType.FØRSTEGANGSSØKNAD,
                TidslinjeHendelseDto.TidslinjeHendelseType.INNTEKTSMELDING,
                TidslinjeHendelseDto.TidslinjeHendelseType.ETTERSENDING,
                TidslinjeHendelseDto.TidslinjeHendelseType.FØRSTEGANGSSØKNAD_NY,
                TidslinjeHendelseDto.TidslinjeHendelseType.VEDTAK
            );
    }

    @Test
    void skalFiltrereBortInnteksmeldingFraJoark() {
        var saksnummer = DUMMY_SAKSNUMMER;
        var søknadMedVedlegg = søknadMed1Vedlegg(saksnummer, LocalDateTime.now());
        var innteksmeldingJournalpost = innteksmeldingJournalpost(saksnummer);
        var inntektsmelding = standardInntektsmelding(LocalDateTime.now());
        when(safselvbetjeningTjeneste.alle(DUMMY_FNR, saksnummer)).thenReturn(
            List.of(søknadMedVedlegg, innteksmeldingJournalpost));
        when(innsyn.inntektsmeldinger(saksnummer)).thenReturn(List.of(inntektsmelding));

        var tidslinje = tjeneste.tidslinje(DUMMY_FNR, saksnummer);

        assertThat(tidslinje)
            .hasSize(2)
            .extracting(TidslinjeHendelseDto::tidslinjeHendelseType)
            .containsExactly(
                TidslinjeHendelseDto.TidslinjeHendelseType.FØRSTEGANGSSØKNAD,
                TidslinjeHendelseDto.TidslinjeHendelseType.INNTEKTSMELDING
            );
    }

    @Test
    void tidslinjenSorteresEtterOpprettetTidspunkt() {
        var saksnummer = DUMMY_SAKSNUMMER;
        var tidspunkt = LocalDateTime.now();
        var inntektsmelding = standardInntektsmelding(tidspunkt);
        var søknadMedVedlegg = søknadMed1Vedlegg(saksnummer, tidspunkt.plusSeconds(1));
        var vedtak = utgåendeVedtak(saksnummer, tidspunkt.plusSeconds(2));
        when(safselvbetjeningTjeneste.alle(DUMMY_FNR, saksnummer)).thenReturn(List.of(vedtak, søknadMedVedlegg)); // Reversert med vilje
        when(innsyn.inntektsmeldinger(saksnummer)).thenReturn(List.of(inntektsmelding));

        var tidslinje = tjeneste.tidslinje(DUMMY_FNR, saksnummer);

        assertThat(tidslinje)
            .hasSize(3)
            .extracting(TidslinjeHendelseDto::tidslinjeHendelseType)
            .containsExactly(
                TidslinjeHendelseDto.TidslinjeHendelseType.INNTEKTSMELDING,
                TidslinjeHendelseDto.TidslinjeHendelseType.FØRSTEGANGSSØKNAD,
                TidslinjeHendelseDto.TidslinjeHendelseType.VEDTAK);
    }

    public static EnkelJournalpost søknadMed1Vedlegg(Saksnummer saksnummer, LocalDateTime mottatt) {
        return new EnkelJournalpost(
            DokumentTypeId.I000003.getTittel(),
            "1",
            saksnummer.value(),
            EnkelJournalpost.DokumentType.INNGÅENDE_DOKUMENT, mottatt,
            DokumentTypeId.I000003,
            List.of(
                new EnkelJournalpost.Dokument("1", DokumentTypeId.I000003.getTittel(), null),
                new EnkelJournalpost.Dokument("2", DokumentTypeId.I000036.getTittel(), null)
            )
        );
    }

    public static EnkelJournalpost endringssøknadUtenVedlegg(Saksnummer saksnummer, LocalDateTime tidspunkt) {
        return new EnkelJournalpost(
            DokumentTypeId.I000050.getTittel(),
            "2",
            saksnummer.value(),
            EnkelJournalpost.DokumentType.INNGÅENDE_DOKUMENT,
            tidspunkt,
            DokumentTypeId.I000050,
            List.of(
                new EnkelJournalpost.Dokument("1", DokumentTypeId.I000050.getTittel(), null)
            )
        );
    }

    public static EnkelJournalpost ettersender2Vedlegg(Saksnummer saksnummer, LocalDateTime tidspunkt) {
        return new EnkelJournalpost(
            DokumentTypeId.I000036.getTittel(),
            "3",
            saksnummer.value(),
            EnkelJournalpost.DokumentType.INNGÅENDE_DOKUMENT, tidspunkt,
            DokumentTypeId.I000023,
            List.of(
                new EnkelJournalpost.Dokument("1", DokumentTypeId.I000023.getTittel(), null),
                new EnkelJournalpost.Dokument("2", DokumentTypeId.I000036.getTittel(), null)
            )
        );
    }

    public static EnkelJournalpost innteksmeldingJournalpost(Saksnummer saksnummer) {
        return new EnkelJournalpost(
            DokumentTypeId.I000067.getTittel(),
            "4",
            saksnummer.value(),
            EnkelJournalpost.DokumentType.INNGÅENDE_DOKUMENT,
            LocalDateTime.now(),
            DokumentTypeId.I000067,
            List.of(
                new EnkelJournalpost.Dokument("1", DokumentTypeId.I000067.getTittel(), null)
            )
        );
    }

    public static EnkelJournalpost utgåendeVedtakFritekts(Saksnummer saksnummer, LocalDateTime mottatt) {
        return new EnkelJournalpost(
            "Innvilgelsesbrev foreldrepenger",
            "5",
            saksnummer.value(),
            EnkelJournalpost.DokumentType.UTGÅENDE_DOKUMENT, mottatt,
            null,
            List.of(
                new EnkelJournalpost.Dokument("1", null, EnkelJournalpost.Brevkode.FRITEKSTBREV)
            )
        );
    }


    public static EnkelJournalpost utgåendeVedtak(Saksnummer saksnummer, LocalDateTime mottatt) {
        return new EnkelJournalpost(
            "Innvilgelsesbrev foreldrepenger",
            "5",
            saksnummer.value(),
            EnkelJournalpost.DokumentType.UTGÅENDE_DOKUMENT, mottatt,
            null,
            List.of(
                new EnkelJournalpost.Dokument("1", null, EnkelJournalpost.Brevkode.FORELDREPENGER_INNVILGELSE)
            )
        );
    }


    public static EnkelJournalpost innhentOpplysningsBrev(Saksnummer saksnummer, LocalDateTime tidspunkt) {
        return new EnkelJournalpost(
            "Innhente opplysninger",
            "5",
            saksnummer.value(),
            EnkelJournalpost.DokumentType.UTGÅENDE_DOKUMENT, tidspunkt,
            null,
            List.of(
                new EnkelJournalpost.Dokument("1", null, EnkelJournalpost.Brevkode.INNHENTE_OPPLYSNINGER)
            )
        );
    }


    public static EnkelJournalpost etterlysIM(Saksnummer saksnummer) {
        return new EnkelJournalpost(
            "Etterlys inntektsmelding",
            "6",
            saksnummer.value(),
            EnkelJournalpost.DokumentType.UTGÅENDE_DOKUMENT,
            LocalDateTime.now(),
            null,
            List.of(
                new EnkelJournalpost.Dokument("1", null, EnkelJournalpost.Brevkode.ETTERLYS_INNTEKTSMELDING)
            )
        );
    }

    public static InntektsmeldingDto standardInntektsmelding(LocalDateTime opprettet) {
        return new InntektsmeldingDto(opprettet);
    }
}
