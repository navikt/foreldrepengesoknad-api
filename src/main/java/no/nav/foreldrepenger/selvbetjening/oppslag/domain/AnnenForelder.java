package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Navn;
import no.nav.foreldrepenger.selvbetjening.util.StringUtil;

@JsonInclude(NON_NULL)
@EqualsAndHashCode
public class AnnenForelder {

    private final String fnr;
    private final String fornavn;
    private final String mellomnavn;
    private final String etternavn;
    private final LocalDate fødselsdato;

    @JsonCreator
    public AnnenForelder(@JsonProperty("fnr") String fnr, @JsonProperty("navn") Navn navn,
            @JsonProperty("fødselsdato") LocalDate fødselsdato) {
        this.fnr = fnr;
        this.fornavn = Optional.ofNullable(navn).map(Navn::getFornavn).orElse(null);
        this.mellomnavn = Optional.ofNullable(navn).map(Navn::getMellomnavn).orElse(null);
        this.etternavn = Optional.ofNullable(navn).map(Navn::getEtternavn).orElse(null);
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
        return getClass().getSimpleName() + "[fnr=" + StringUtil.maskFnr(fnr) + ", fornavn=" + fornavn + ", mellomnavn=" + mellomnavn
                + ", etternavn=" + etternavn + ", fødselsdato=" + fødselsdato + "]";
    }
}
