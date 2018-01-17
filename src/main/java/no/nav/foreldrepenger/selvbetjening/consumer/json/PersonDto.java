package no.nav.foreldrepenger.selvbetjening.consumer.json;

import java.time.LocalDate;

public class PersonDto {

    public String fnr;
    public String aktorId;
    public String fornavn;
    public String etternavn;
    public String kjonn;
    public LocalDate fodselsdato;

    public AdresseDto adresse;

}
