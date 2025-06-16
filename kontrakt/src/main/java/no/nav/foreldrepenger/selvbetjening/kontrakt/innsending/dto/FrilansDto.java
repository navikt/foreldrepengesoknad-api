package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record FrilansDto(boolean jobberFremdelesSomFrilans, @NotNull LocalDate oppstart) {
}
