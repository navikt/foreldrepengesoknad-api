package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record ÅpenPeriodeDto(@NotNull LocalDate fom, LocalDate tom) {
}
