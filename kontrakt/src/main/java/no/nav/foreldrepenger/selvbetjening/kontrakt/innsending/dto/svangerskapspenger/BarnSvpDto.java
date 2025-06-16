package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record BarnSvpDto(@NotNull LocalDate termindato, LocalDate fødselsdato) {
}
