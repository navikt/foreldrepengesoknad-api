package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto;

import java.time.LocalDate;

public record FrilansDto(boolean jobberFremdelesSomFrilans, LocalDate oppstart) {
}
