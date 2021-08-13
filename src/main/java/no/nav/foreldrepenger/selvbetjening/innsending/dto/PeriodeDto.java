package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_NULL)
public record PeriodeDto(LocalDate fom, LocalDate tom) {
    public PeriodeDto(LocalDate fom) {
        this(fom, null);
    }

}
