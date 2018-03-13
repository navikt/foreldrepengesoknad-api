package no.nav.foreldrepenger.selvbetjening.rest.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import no.nav.foreldrepenger.selvbetjening.consumer.json.PersonDto;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class Person {

    public Person(PersonDto personDto) {
        this.fornavn = personDto.fornavn;
        this.etternavn = personDto.etternavn;
        this.kjønn = personDto.kjonn;
        this.fødselsdato = personDto.fodselsdato;
    }

    public Person(String fornavn, String mellomnavn, String etternavn, String kjønn, LocalDate fødselsdato) {
        this.fornavn = fornavn;
        this.mellomnavn = mellomnavn;
        this.etternavn = etternavn;
        this.kjønn = kjønn;
        this.fødselsdato = fødselsdato;
    }

    public String fornavn;
    public String mellomnavn;
    public String etternavn;
    public String kjønn;
    public LocalDate fødselsdato;

}
