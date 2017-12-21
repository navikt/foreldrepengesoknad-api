package no.nav.foreldrepenger.selvbetjening.engangstonad.rest.json;

public class Person {

    public Person(String fornavn, String mellomnavn, String etternavn, String kjonn, Integer alder) {
        this.fornavn = fornavn;
        this.mellomnavn = mellomnavn;
        this.etternavn = etternavn;
        this.kjonn = kjonn;
        this.alder = alder;
    }

    public Person(String fornavn, String etternavn) {
        this.fornavn = fornavn;
        this.etternavn = etternavn;
    }

    public String fornavn;
    public String mellomnavn;
    public String etternavn;
    public String kjonn;
    public Integer alder;

}
