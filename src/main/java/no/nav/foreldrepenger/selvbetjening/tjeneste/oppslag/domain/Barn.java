package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_NULL)
public class Barn {

    public final String fornavn;
    public final String mellomnavn;
    public final String etternavn;
    public final String fnr;
    public final String kjønn;
    public final LocalDate fødselsdato;
    public final AnnenForelder annenForelder;

    public Barn(String fnr, String fornavn, String mellomnavn, String etternavn, String kjønn, LocalDate fødselsdato,
            AnnenForelder annenForelder) {
        this.fnr = fnr;
        this.fornavn = fornavn;
        this.mellomnavn = mellomnavn;
        this.etternavn = etternavn;
        this.kjønn = kjønn;
        this.fødselsdato = fødselsdato;
        this.annenForelder = annenForelder;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[fornavn=" + fornavn + ", mellomnavn=" + mellomnavn + ", etternavn="
                + etternavn + ", fnr=" + fnr + ", kjønn=" + kjønn + ", fødselsdato=" + fødselsdato + ", annenForelder="
                + annenForelder + "]";
    }

}
