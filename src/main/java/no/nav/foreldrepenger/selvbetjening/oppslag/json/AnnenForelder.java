package no.nav.foreldrepenger.selvbetjening.oppslag.json;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_NULL)
public class AnnenForelder {

    public String fnr;
    public String fornavn;
    public String mellomnavn;
    public String etternavn;
    public LocalDate fødselsdato;

    public AnnenForelder() {}

    public AnnenForelder(String fnr, String fornavn, String mellomnavn, String etternavn, LocalDate fødselsdato) {
        this.fnr = fnr;
        this.fornavn = fornavn;
        this.mellomnavn = mellomnavn;
        this.etternavn = etternavn;
        this.fødselsdato = fødselsdato;
    }
}
