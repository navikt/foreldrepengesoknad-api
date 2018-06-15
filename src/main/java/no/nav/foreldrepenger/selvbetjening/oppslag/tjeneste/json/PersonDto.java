package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.selvbetjening.oppslag.json.Bankkonto;
import no.nav.foreldrepenger.selvbetjening.oppslag.json.Barn;

import static com.fasterxml.jackson.annotation.JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY;

public class PersonDto {

    public String fnr;
    public String aktorId;
    public String fornavn;
    public String mellomnavn;
    public String etternavn;
    public String kjonn;
    public LocalDate fodselsdato;
    public String målform;
    public CountryCode landKode;
    public Bankkonto bankkonto;
    public List<Barn> barn;

    public PersonDto() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDto personDto = (PersonDto) o;
        return Objects.equals(fnr, personDto.fnr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fnr);
    }

    @Override
    public String toString() {
        return "PersonDto { fnr='" + fnr + "' }";
    }
}
