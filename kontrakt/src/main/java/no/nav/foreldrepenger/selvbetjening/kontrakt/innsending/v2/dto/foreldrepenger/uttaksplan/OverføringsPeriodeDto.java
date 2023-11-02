package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.Overføringsårsak;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;

public record OverføringsPeriodeDto(@NotNull LocalDate fom,
                                    @NotNull LocalDate tom,
                                    @NotNull Overføringsårsak årsak,
                                    @NotNull KontoType konto,
                                    @Size(max = 100) List<@Valid @NotNull MutableVedleggReferanseDto> vedleggsreferanser) implements Uttaksplanperiode {
}
