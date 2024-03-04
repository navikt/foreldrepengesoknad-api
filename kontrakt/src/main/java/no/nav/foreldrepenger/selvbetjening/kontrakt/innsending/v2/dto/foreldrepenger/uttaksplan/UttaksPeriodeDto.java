package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.MorsAktivitet;

public record UttaksPeriodeDto(@NotNull LocalDate fom,
                               @NotNull LocalDate tom,
                               @NotNull KontoType konto,
                               MorsAktivitet morsAktivitetIPerioden,
                               Boolean ønskerSamtidigUttak,
                               Boolean justeresVedFødsel,
                               Boolean ønskerFlerbarnsdager,
                               @Min(0) @Max(100) Double samtidigUttakProsent) implements Uttaksplanperiode {
}
