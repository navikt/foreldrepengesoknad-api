package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.Oppholdsårsak;

public record OppholdsPeriodeDto(@NotNull LocalDate fom, @NotNull LocalDate tom, @NotNull Oppholdsårsak årsak) implements Uttaksplanperiode {
}
