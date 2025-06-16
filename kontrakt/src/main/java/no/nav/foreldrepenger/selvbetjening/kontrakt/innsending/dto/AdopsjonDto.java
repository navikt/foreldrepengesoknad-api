package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record AdopsjonDto(@Min(1) @Max(Integer.MAX_VALUE) int antallBarn,
                          @Size(min = 1, max = 10) List<@PastOrPresent LocalDate> fødselsdatoer,
                          @NotNull LocalDate adopsjonsdato,
                          LocalDate ankomstdato,
                          @NotNull Boolean adopsjonAvEktefellesBarn,
                          Boolean søkerAdopsjonAlene) implements BarnDto {
}
