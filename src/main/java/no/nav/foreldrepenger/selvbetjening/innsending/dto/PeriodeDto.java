package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import java.time.LocalDate;

public record PeriodeDto(LocalDate fom, LocalDate tom) {

    public static PeriodeDto open(LocalDate fom) {
        return new PeriodeDto(fom, null);
    }
}
