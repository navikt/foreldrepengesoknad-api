package no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public record TidslinjeHendelseDto(@NotNull LocalDateTime opprettet,
                                   @NotNull AktørType aktørType,
                                   @NotNull TidslinjeHendelseType tidslinjeHendelseType,
                                   @NotNull List<@Valid @NotNull Dokument> dokumenter) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TidslinjeHendelseDto that = (TidslinjeHendelseDto) o;
        return aktørType == that.aktørType
                && Objects.equals(opprettet, that.opprettet)
                && erDokumenterLike(dokumenter, that.dokumenter)
                && tidslinjeHendelseType == that.tidslinjeHendelseType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(opprettet, aktørType, tidslinjeHendelseType, dokumenter);
    }

    private boolean erDokumenterLike(List<Dokument> gammel, List<Dokument> ny) {
        if (Objects.equals(gammel, ny)) return true;
        if (gammel == null || ny == null) return false;
        if (gammel.size() != ny.size()) return false;

        return gammel.stream().allMatch(b ->
                Collections.frequency(gammel, b) == Collections.frequency(ny, b)
        );
    }

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
