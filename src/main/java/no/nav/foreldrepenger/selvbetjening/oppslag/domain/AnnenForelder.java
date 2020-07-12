package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(NON_NULL)
public class AnnenForelder {

    private final String fnr;
    private final String fornavn;
    private final String mellomnavn;
    private final String etternavn;
    private final LocalDate fødselsdato;

    @JsonCreator
    public AnnenForelder(@JsonProperty("fnr") String fnr, @JsonProperty("fornavn") String fornavn,
            @JsonProperty("mellomnavn") String mellomnavn,
            @JsonProperty("etternavn") String etternavn,
            @JsonProperty("fødselsdato") LocalDate fødselsdato) {
        this.fnr = fnr;
        this.fornavn = fornavn;
        this.mellomnavn = mellomnavn;
        this.etternavn = etternavn;
        this.fødselsdato = fødselsdato;
    }

    public String getFnr() {
        return fnr;
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

    public LocalDate getFødselsdato() {
        return fødselsdato;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[fnr=" + fnr + ", fornavn=" + fornavn + ", mellomnavn=" + mellomnavn
                + ", etternavn=" + etternavn + ", fødselsdato=" + fødselsdato + "]";
    }
}
