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

    public PersonDto(String fnr, String aktorId, String fornavn, String mellomnavn, String etternavn, String målform,
            CountryCode landKode, Bankkonto bankkonto) {
        this.fnr = fnr;
        this.aktorId = aktorId;
        this.fornavn = fornavn;
        this.mellomnavn = mellomnavn;
        this.etternavn = etternavn;
        this.målform = målform;
        this.landKode = landKode;
        this.bankkonto = bankkonto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDto personDto = (PersonDto) o;
        return Objects.equals(fnr, personDto.fnr) &&
                Objects.equals(aktorId, personDto.aktorId) &&
                Objects.equals(fornavn, personDto.fornavn) &&
                Objects.equals(mellomnavn, personDto.mellomnavn) &&
                Objects.equals(etternavn, personDto.etternavn) &&
                Objects.equals(kjonn, personDto.kjonn) &&
                Objects.equals(fodselsdato, personDto.fodselsdato) &&
                Objects.equals(målform, personDto.målform) &&
                landKode == personDto.landKode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fnr, aktorId, fornavn, mellomnavn, etternavn, kjonn, fodselsdato, målform, landKode);
    }

    @Override
    public String toString() {
        return "PersonDto { fnr='" + fnr + "' }";
    }
}
