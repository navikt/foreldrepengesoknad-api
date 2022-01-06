package no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2.persondetaljer;

import com.fasterxml.jackson.annotation.JsonCreator;
import no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2.PersonDetaljer;

import java.time.LocalDate;
import java.util.Objects;


public record Person (Fødselsnummer fødselsnummer,
                      String fornavn,
                      String mellomnavn,
                      String etternavn,
                      Kjønn kjønn,
                      LocalDate fødselsdato) implements PersonDetaljer {

    @JsonCreator
    public Person {
        Objects.requireNonNull(fødselsnummer,"Fødselsnummer kan ikke være null");
        Objects.requireNonNull(fornavn,"Fornavn kan ikke være null");
        Objects.requireNonNull(fornavn,"Etternavn kan ikke være null");
    }

}
