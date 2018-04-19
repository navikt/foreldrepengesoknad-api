package no.nav.foreldrepenger.selvbetjening.consumer.json;

import java.time.LocalDate;

import com.neovisionaries.i18n.CountryCode;

public class PersonDto {

    public String fnr;
    public String aktorId;
    public String fornavn;
    public String mellomnavn;
    public String etternavn;
    public String kjonn;
    public LocalDate fodselsdato;
    public String målform;
    public CountryCode land;

    public PersonDto() {
    }

    public PersonDto(String fnr, String aktorId, String fornavn, String mellomnavn, String etternavn, String målform,
            CountryCode land) {
        this.fnr = fnr;
        this.aktorId = aktorId;
        this.fornavn = fornavn;
        this.mellomnavn = mellomnavn;
        this.etternavn = etternavn;
        this.målform = målform;
        this.land = land;
    }

}
