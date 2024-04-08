package no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje;

import java.time.LocalDateTime;
import java.util.List;

public record TidslinjeHendelseDto(LocalDateTime opprettet,
                                   AktørType aktørType,
                                   TidslinjeHendelseType tidslinjeHendelseType,
                                   List<Dokument> dokumenter) {

    enum AktørType {
        BRUKER,
        NAV,
        ARBEIDSGIVER
    }

    enum TidslinjeHendelseType {
        FØRSTEGANGSSØKNAD,
        FØRSTEGANGSSØKNAD_NY,
        KLAGE,
        ETTERSENDING,
        ENDRINGSSØKNAD,
        INNTEKTSMELDING,

        // Utgående hendelsestyper
        VEDTAK,
        VEDTAK_KLAGE,
        UTGÅENDE_INNHENT_OPPLYSNINGER,
        UTGÅENDE_ETTERLYS_INNTEKTSMELDING,
        UTGÅENDE_KLAGE_SENDT_TIL_KLAGEINSTANSEN,
        UTGÅENDE_VARSEL_TILBAKEBETALING,
        FORELDREPENGER_FEIL_PRAKSIS_UTSETTELSE_INFOBREV,
    }

    public record Dokument(String journalpostId, String dokumentId, String tittel) {
    }
}
