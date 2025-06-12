package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record ÅpenPeriodeDtoOLD(@NotNull LocalDate fom, LocalDate tom) {
}
