package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad;

import static java.util.Collections.emptyList;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;

public record TerminDto(@Min(1) @Max(Integer.MAX_VALUE) int antallBarn,
                        @NotNull LocalDate termindato,
                        LocalDate terminbekreftelseDato,
                        @Valid @Size(max = 15) List<@Valid MutableVedleggReferanseDto> vedleggreferanser) implements BarnDto {

    public TerminDto {
        vedleggreferanser = Optional.ofNullable(vedleggreferanser).orElse(emptyList());
    }
}
