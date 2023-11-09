package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record FødselDto(@Min(1) @Max(Integer.MAX_VALUE) int antallBarn,
                        @NotNull LocalDate fødselsdato,
                        LocalDate termindato) implements BarnDto {
}
