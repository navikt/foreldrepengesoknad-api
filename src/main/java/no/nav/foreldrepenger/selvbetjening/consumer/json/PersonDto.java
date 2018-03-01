package no.nav.foreldrepenger.selvbetjening.consumer.json;

import java.time.LocalDate;

public class PersonDto {

    public String fnr;
    public String aktørId;
    public String fornavn;
    public String etternavn;
    public String kjønn;
    public LocalDate fødselsdato;

    public AdresseDto adresse;

}
