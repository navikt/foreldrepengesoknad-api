package no.nav.foreldrepenger.selvbetjening.rest.json;

import no.nav.foreldrepenger.selvbetjening.consumer.json.PersonDto;

public class Person {

    public Person(PersonDto personDto) {
        this.fornavn = personDto.name.fornavn;
        this.mellomnavn = personDto.name.mellomnavn;
        this.etternavn = personDto.name.etternavn;

        this.adresse = personDto.adresse.adresse();
    }

    public Person(String fornavn, String etternavn, String adresse) {
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.adresse = adresse;
    }

    public String fornavn;
    public String mellomnavn;
    public String etternavn;
    public String kjonn;
    public String adresse;

}
