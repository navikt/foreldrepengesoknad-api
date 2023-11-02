package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UttaksplanDto(Boolean ønskerJustertUttakVedFødsel,
                            @Size(min = 1, max = 200) List<@Valid @NotNull Uttaksplanperiode> uttaksperioder) {
}
