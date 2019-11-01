package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_NULL)
public class AnnenForelder {

    public final String fnr;
    public final String fornavn;
    public final String mellomnavn;
    public final String etternavn;
    public final LocalDate fødselsdato;

    public AnnenForelder(String fnr, String fornavn, String mellomnavn, String etternavn, LocalDate fødselsdato) {
        this.fnr = fnr;
        this.fornavn = fornavn;
        this.mellomnavn = mellomnavn;
        this.etternavn = etternavn;
        this.fødselsdato = fødselsdato;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[fnr=" + fnr + ", fornavn=" + fornavn + ", mellomnavn=" + mellomnavn
                + ", etternavn=" + etternavn + ", fødselsdato=" + fødselsdato + "]";
    }
}
