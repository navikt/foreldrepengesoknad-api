package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json;

import java.time.LocalDate;
import java.util.Objects;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.selvbetjening.oppslag.json.Bankkonto;

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

    public PersonDto() {
        this.bankkonto = new Bankkonto();
    }

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
