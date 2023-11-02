package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.MorsAktivitet;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;

public record UttaksPeriodeDto(@NotNull LocalDate fom,
                               @NotNull LocalDate tom,
                               @NotNull KontoType konto,
                               MorsAktivitet morsAktivitetIPerioden,
                               Boolean ønskerSamtidigUttak,
                               Boolean justeresVedFødsel,
                               Boolean ønskerFlerbarnsdager,
                               @Min(0) @Max(100) Double samtidigUttakProsent,
                               @Size(max = 100) List<@Valid @NotNull MutableVedleggReferanseDto> vedleggsreferanser) implements Uttaksplanperiode {

}
