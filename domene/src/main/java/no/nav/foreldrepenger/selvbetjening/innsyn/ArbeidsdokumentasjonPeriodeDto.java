package no.nav.foreldrepenger.selvbetjening.innsyn;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record ArbeidsdokumentasjonPeriodeDto(@Size(max = 50) List<@Valid @NotNull Periode> perioder) {

    public record Periode(@NotNull LocalDate fom, @NotNull LocalDate tom) {
    }
}
