package no.nav.foreldrepenger.selvbetjening.oppslag.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record Søkerinfo(@NotNull @Valid PersonFrontend søker, @NotNull @Size List<@Valid @NotNull Arbeidsforhold> arbeidsforhold) {

}
