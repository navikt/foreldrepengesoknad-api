package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OmsorgsovertakelseDto(@Min(1) @Max(Integer.MAX_VALUE) int antallBarn,
                                    @Size(min = 1, max = 10) List<LocalDate> fødselsdatoer,
                                    @NotNull LocalDate foreldreansvarsdato) implements BarnDto {
}
