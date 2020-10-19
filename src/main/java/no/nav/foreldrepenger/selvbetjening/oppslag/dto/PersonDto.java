package no.nav.foreldrepenger.selvbetjening.oppslag.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Navn;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Bankkonto;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Barn;

public class PersonDto {

    @JsonAlias("id")
    public String fnr;
    public String aktorId;
    public Navn navn;
    public LocalDate fødselsdato;
    public String målform;
    public CountryCode landKode;
    public Bankkonto bankkonto;
    public List<Barn> barn;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        PersonDto personDto = (PersonDto) o;
        return Objects.equals(fnr, personDto.fnr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fnr);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [fnr=" + fnr + ", aktorId=" + aktorId + ", navn=" + navn
                + ", fødselsdato=" + fødselsdato + ", målform="
                + målform + ", landKode=" + landKode + ", bankkonto=" + bankkonto + ", barn=" + barn + "]";
    }

}
