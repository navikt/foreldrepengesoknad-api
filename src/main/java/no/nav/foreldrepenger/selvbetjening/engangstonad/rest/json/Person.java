package no.nav.foreldrepenger.selvbetjening.engangstonad.rest.json;

public class Person {

    public Person(String fornavn, String mellomnavn, String etternavn, String kjonn, String adresse) {
        this.fornavn = fornavn;
        this.mellomnavn = mellomnavn;
        this.etternavn = etternavn;
        this.kjonn = kjonn;
        this.adresse = adresse;
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
