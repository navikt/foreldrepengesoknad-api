package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_NULL)
public class PeriodeDto {
    public LocalDate fom;
    public LocalDate tom;
}
