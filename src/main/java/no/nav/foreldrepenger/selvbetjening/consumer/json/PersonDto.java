package no.nav.foreldrepenger.selvbetjening.consumer.json;

import java.time.LocalDate;

public class PersonDto {

    public PersonDto() {}

    public PersonDto(String fnr, String aktorId, String fornavn, String mellomnavn, String etternavn, String målform) {
        this.fnr = fnr;
        this.aktorId = aktorId;
        this.fornavn = fornavn;
        this.mellomnavn = mellomnavn;
        this.etternavn = etternavn;
        this.målform = målform;
    }

    public String fnr;
    public String aktorId;
    public String fornavn;
    public String mellomnavn;
    public String etternavn;
    public String kjonn;
    public LocalDate fodselsdato;
    public String målform;

    public AdresseDto adresse;

}
