package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(NON_NULL)
public class Barn {

    private final String fornavn;
    private final String mellomnavn;
    private final String etternavn;
    private final String fnr;
    private final String kjønn;
    private final LocalDate fødselsdato;
    private final AnnenForelder annenForelder;

    @JsonCreator
    public Barn(@JsonProperty("fnr") String fnr, @JsonProperty("fornavn") String fornavn,
            @JsonProperty("mellomnavn") String mellomnavn,
            @JsonProperty("etternavn") String etternavn, @JsonProperty("kjønn") String kjønn,
            @JsonProperty("fødselsdato") LocalDate fødselsdato,
            @JsonProperty("annenForelder") AnnenForelder annenForelder) {
        this.fnr = fnr;
        this.fornavn = fornavn;
        this.mellomnavn = mellomnavn;
        this.etternavn = etternavn;
        this.kjønn = kjønn;
        this.fødselsdato = fødselsdato;
        this.annenForelder = annenForelder;
    }

    public String getFornavn() {
        return fornavn;
    }

    public String getMellomnavn() {
        return mellomnavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public String getFnr() {
        return fnr;
    }

    public String getKjønn() {
        return kjønn;
    }

    public LocalDate getFødselsdato() {
        return fødselsdato;
    }

    public AnnenForelder getAnnenForelder() {
        return annenForelder;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[fornavn=" + fornavn + ", mellomnavn=" + mellomnavn + ", etternavn="
                + etternavn + ", fnr=" + fnr + ", kjønn=" + kjønn + ", fødselsdato=" + fødselsdato + ", annenForelder="
                + annenForelder + "]";
    }

}
