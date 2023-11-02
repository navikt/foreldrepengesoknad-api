package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.Oppholdsårsak;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;

public record OppholdsPeriodeDto(@NotNull LocalDate fom,
                                 @NotNull LocalDate tom,
                                 @NotNull Oppholdsårsak årsak,
                                 @Valid @Size(max = 100) List<@Valid @NotNull MutableVedleggReferanseDto> vedleggsreferanser) implements Uttaksplanperiode {
    public OppholdsPeriodeDto {
        vedleggsreferanser = Optional.ofNullable(vedleggsreferanser).map(ArrayList::new).orElse(new ArrayList<>());
    }
}
