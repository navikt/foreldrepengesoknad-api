package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Navn;
import no.nav.foreldrepenger.selvbetjening.util.StringUtil;

@JsonInclude(NON_NULL)
public record AnnenForelder(@JsonProperty("fnr") String fnr, @JsonProperty("navn") Navn navn,
        @JsonProperty("fødselsdato") LocalDate fødselsdato) {

    public String getFornavn() {
        return Optional.ofNullable(navn()).map(Navn::fornavn).orElse(null);
    }

    public String getMellomnavn() {
        return Optional.ofNullable(navn()).map(Navn::mellomnavn).orElse(null);
    }

    public String getEtternavn() {
        return Optional.ofNullable(navn()).map(Navn::etternavn).orElse(null);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[fnr=" + StringUtil.maskFnr(fnr) + ", fornavn=" + getFornavn() + ", mellomnavn="
                + getMellomnavn()
                + ", etternavn=" + getEtternavn() + ", fødselsdato=" + fødselsdato + "]";
    }
}
