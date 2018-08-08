package no.nav.foreldrepenger.selvbetjening.oppslag.json;

import java.time.LocalDate;

public class Barn {

    public String fornavn;
    public String mellomnavn;
    public String etternavn;
    public String fnr;
    public String kjønn;
    public LocalDate fødselsdato;
    public AnnenForelder annenForelder;

    @SuppressWarnings("unused")
    public Barn() {

    }

    public Barn(String fnr, String fornavn, String mellomnavn, String etternavn, String kjønn, LocalDate fødselsdato, AnnenForelder annenForelder) {
        this.fnr = fnr;
        this.fornavn = fornavn;
        this.mellomnavn = mellomnavn;
        this.etternavn = etternavn;
        this.kjønn = kjønn;
        this.fødselsdato = fødselsdato;
        this.annenForelder = annenForelder;
    }

}
