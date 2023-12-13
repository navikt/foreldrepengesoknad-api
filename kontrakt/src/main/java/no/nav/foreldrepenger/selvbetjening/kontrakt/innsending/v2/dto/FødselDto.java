package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;

public record FødselDto(@Min(1) @Max(Integer.MAX_VALUE) int antallBarn,
                        @NotNull LocalDate fødselsdato,
                        LocalDate termindato,
                        @Valid @Size(max = 15) List<@Valid MutableVedleggReferanseDto> vedleggreferanser) implements BarnDto {

    public FødselDto {
        vedleggreferanser = Optional.ofNullable(vedleggreferanser).map(ArrayList::new).orElse(new ArrayList<>());
    }
}
