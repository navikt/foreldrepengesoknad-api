package no.nav.foreldrepenger.selvbetjening.rest.json;

import no.nav.foreldrepenger.selvbetjening.consumer.json.PersonDto;

import java.time.LocalDate;

public class Person {

    public Person(PersonDto personDto) {
        this.fornavn = personDto.fornavn;
        this.etternavn = personDto.etternavn;
        this.kjonn = personDto.kjonn;
        this.fodselsdato = personDto.fodselsdato;
        this.adresse = personDto.adresse.adresse();
    }

    public Person(String fornavn, String etternavn, String kjonn, LocalDate fodselsdato, String adresse) {
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.kjonn = kjonn;
        this.fodselsdato = fodselsdato;
        this.adresse = adresse;
    }

    public String fornavn;
    public String mellomnavn;
    public String etternavn;
    public String kjonn;
    public LocalDate fodselsdato;
    public String adresse;

}
