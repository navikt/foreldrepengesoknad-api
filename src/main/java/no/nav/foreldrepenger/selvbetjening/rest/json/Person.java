package no.nav.foreldrepenger.selvbetjening.rest.json;

import no.nav.foreldrepenger.selvbetjening.consumer.json.PersonDto;

import java.time.LocalDate;

public class Person {

    public Person(PersonDto personDto) {
        this.fornavn = personDto.fornavn;
        this.etternavn = personDto.etternavn;
        this.kjønn = personDto.kjonn;
        this.fødselsdato = personDto.fodselsdato;
        this.adresse = personDto.adresse.adresse();
    }

    public Person(String fornavn, String etternavn, String kjønn, LocalDate fødselsdato, String adresse) {
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.kjønn = kjønn;
        this.fødselsdato = fødselsdato;
        this.adresse = adresse;
    }

    public String fornavn;
    public String mellomnavn;
    public String etternavn;
    public String kjønn;
    public LocalDate fødselsdato;
    public String adresse;

}
