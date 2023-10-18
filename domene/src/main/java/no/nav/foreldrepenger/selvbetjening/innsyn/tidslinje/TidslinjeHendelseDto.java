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
        ETTERSENDING,
        ENDRINGSSØKNAD,
        INNTEKTSMELDING,
        VEDTAK,
        UTGÅENDE_INNHENT_OPPLYSNINGER,
        UTGÅENDE_ETTERLYS_INNTEKTSMELDING,
        UTGÅENDE_VARSEL_TILBAKEBETALING
    }

    public record Dokument(String journalpostId, String dokumentId, String tittel) {
    }
}
