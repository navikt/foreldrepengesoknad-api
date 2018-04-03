package no.nav.foreldrepenger.selvbetjening.consumer.json;

import java.time.LocalDate;

public class PersonDto {

    public PersonDto() {}

    public PersonDto(String fnr, String aktorId, String fornavn, String etternavn) {
        this.fnr = fnr;
        this.aktorId = aktorId;
        this.fornavn = fornavn;
        this.etternavn = etternavn;
    }

    public String fnr;
    public String aktorId;
    public String fornavn;
    public String etternavn;
    public String kjonn;
    public LocalDate fodselsdato;

    public AdresseDto adresse;

}
