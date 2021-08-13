package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Navn;
import no.nav.foreldrepenger.selvbetjening.util.StringUtil;

@JsonInclude(NON_NULL)
@EqualsAndHashCode
public class Barn {

    private final Navn navn;
    private final String fnr;
    private final LocalDate fødselsdato;
    private final AnnenForelder annenForelder;

    @JsonCreator
    public Barn(@JsonProperty("fnr") String fnr, @JsonProperty("navn") Navn navn,
            @JsonProperty("fødselsdato") LocalDate fødselsdato,
            @JsonProperty("annenForelder") @JsonAlias("annenPart") AnnenForelder annenForelder) {
        this.fnr = fnr;
        this.navn = navn;
        this.fødselsdato = fødselsdato;
        this.annenForelder = annenForelder;
    }

    public String getFornavn() {
        return navn.fornavn();
    }

    public String getMellomnavn() {
        return navn.mellomnavn();
    }

    public String getEtternavn() {
        return navn.etternavn();
    }

    public String getFnr() {
        return fnr;
    }

    public String getKjønn() {
        return navn.kjønn().name();
    }

    public LocalDate getFødselsdato() {
        return fødselsdato;
    }

    public AnnenForelder getAnnenForelder() {
        return annenForelder;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [navn=" + navn + ", fnr=" + StringUtil.maskFnr(fnr) + ", fødselsdato=" + fødselsdato
                + ", annenForelder=" + annenForelder
                + "]";
    }
}
