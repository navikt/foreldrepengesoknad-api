package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record FødselDto(@Min(1) @Max(Integer.MAX_VALUE) int antallBarn,
                        @NotNull @PastOrPresent LocalDate fødselsdato,
                        LocalDate termindato) implements BarnDto {
}
