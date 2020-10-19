package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Navn;

@JsonInclude(NON_NULL)
public class Barn {

    private final Navn navn;
    private final String fnr;
    private final LocalDate fødselsdato;
    private final AnnenForelder annenForelder;

    @JsonCreator
    public Barn(@JsonProperty("fnr") String fnr, @JsonProperty("barn") Navn navn,
            @JsonProperty("fødselsdato") LocalDate fødselsdato,
            @JsonProperty("annenForelder") @JsonAlias("annenPart") AnnenForelder annenForelder) {
        this.fnr = fnr;
        this.navn = navn;
        this.fødselsdato = fødselsdato;
        this.annenForelder = annenForelder;
    }

    public String getFornavn() {
        return navn.getFornavn();
    }

    public String getMellomnavn() {
        return navn.getMellomnavn();
    }

    public String getEtternavn() {
        return navn.getEtternavn();
    }

    public String getFnr() {
        return fnr;
    }

    public String getKjønn() {
        return navn.getKjønn().name();
    }

    public LocalDate getFødselsdato() {
        return fødselsdato;
    }

    public AnnenForelder getAnnenForelder() {
        return annenForelder;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[navn=" + navn + ", fødselsdato=" + fødselsdato + ", annenForelder=" + annenForelder + "]";
    }

}
