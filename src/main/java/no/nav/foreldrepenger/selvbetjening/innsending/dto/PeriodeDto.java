package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import java.time.LocalDate;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Tidsperiode;

public record PeriodeDto(LocalDate fom, LocalDate tom) {

    public PeriodeDto(Tidsperiode p) {
        this(p.fom(), p.tom());
    }

    public PeriodeDto(LocalDate fom) {
        this(fom, null);
    }

    public static PeriodeDto of(LocalDate fom, LocalDate tom) {
        return new PeriodeDto(fom, tom);
    }

}
