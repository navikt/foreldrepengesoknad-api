package no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record TidslinjeHendelseDto(@NotNull LocalDateTime opprettet,
                                   @NotNull AktørType aktørType,
                                   @NotNull TidslinjeHendelseType tidslinjeHendelseType,
                                   @NotNull List<@Valid @NotNull Dokument> dokumenter) {
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
        FORELDREPENGER_FEIL_PRAKSIS_UTSETTELSE_INFOBREV,
        UTGÅENDE_VARSEL_TILBAKEBETALING
    }

    public record Dokument(@NotNull String journalpostId, @NotNull String dokumentId, @NotNull String tittel) {
    }
}
