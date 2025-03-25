package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotNull;

@JsonInclude(NON_NULL)
public record Søkerinfo(@NotNull PersonFrontend søker, @NotNull List<Arbeidsforhold> arbeidsforhold) {

}
