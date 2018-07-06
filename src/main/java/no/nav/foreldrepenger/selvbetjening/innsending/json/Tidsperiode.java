package no.nav.foreldrepenger.selvbetjening.innsending.json;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class Tidsperiode {
    public LocalDate startdato;
    public LocalDate sluttdato;
}
