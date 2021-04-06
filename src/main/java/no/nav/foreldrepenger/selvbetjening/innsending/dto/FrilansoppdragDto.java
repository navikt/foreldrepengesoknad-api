package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import java.time.LocalDate;

public class FrilansoppdragDto {
    @Override
    public String toString() {
        return "FrilansoppdragDto [oppdragsgiver=" + oppdragsgiver + ", periode=" + periode + "]";
    }

    public String oppdragsgiver;
    public PeriodeDto periode;

    public FrilansoppdragDto(String oppdragsgiver, LocalDate fom, LocalDate tom) {
        this.oppdragsgiver = oppdragsgiver;
        this.periode = new PeriodeDto(fom, tom);
    }
}