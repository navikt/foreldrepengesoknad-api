package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record FrilansDto(boolean jobberFremdelesSomFrilans, @NotNull LocalDate oppstart) {
}
