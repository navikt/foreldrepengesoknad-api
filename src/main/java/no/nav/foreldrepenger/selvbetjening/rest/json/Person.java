package no.nav.foreldrepenger.selvbetjening.rest.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import no.nav.foreldrepenger.selvbetjening.consumer.json.PersonDto;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class Person {

    public Person(PersonDto personDto) {
        this.fnr = personDto.fnr;
        this.fornavn = personDto.fornavn;
        this.etternavn = personDto.etternavn;
        this.mellomnavn = personDto.mellomnavn;
        this.kjønn = personDto.kjonn;
        this.fødselsdato = personDto.fodselsdato;
        this.målform = personDto.målform;
    }

    public String fnr;
    public String fornavn;
    public String mellomnavn;
    public String etternavn;
    public String kjønn;
    public LocalDate fødselsdato;
    public String målform;

}
