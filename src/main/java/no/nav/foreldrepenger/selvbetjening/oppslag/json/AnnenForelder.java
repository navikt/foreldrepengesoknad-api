package no.nav.foreldrepenger.selvbetjening.oppslag.json;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class AnnenForelder {

    public String fnr;
    public String fornavn;
    public String mellomnavn;
    public String etternavn;
    public LocalDate fødselsdato;

}
