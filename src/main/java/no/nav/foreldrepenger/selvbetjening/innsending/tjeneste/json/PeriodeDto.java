package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class PeriodeDto {
    public LocalDate fom;
    public LocalDate tom;
}
