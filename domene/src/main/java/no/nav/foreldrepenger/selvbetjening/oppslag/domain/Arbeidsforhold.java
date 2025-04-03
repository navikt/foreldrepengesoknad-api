package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.NotNull;

public record Arbeidsforhold(@NotNull String arbeidsgiverId,
                             @NotNull String arbeidsgiverIdType,
                             @NotNull String arbeidsgiverNavn,
                             @NotNull Double stillingsprosent,
                             @NotNull @JsonAlias("from") LocalDate fom,
                             @JsonAlias("to") LocalDate tom) {

}
