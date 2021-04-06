package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import java.time.LocalDate;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Tidsperiode;

public record FrilansoppdragDto(String oppdragsgiver, PeriodeDto periode) {
    public FrilansoppdragDto(String oppdragsgiver, LocalDate fom, LocalDate tom) {
        this(oppdragsgiver, PeriodeDto.of(fom, tom));
    }

    public FrilansoppdragDto(String oppdragsgiver, Tidsperiode p) {
        this(oppdragsgiver, new PeriodeDto(p));
    }
}