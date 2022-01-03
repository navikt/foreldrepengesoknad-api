package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public final class Engangsstønad extends Søknad {

    @Builder
    @JsonCreator
    public Engangsstønad(LocalDateTime opprettet, String type, String saksnummer, Søker søker, Barn barn,
                         AnnenForelder annenForelder, Utenlandsopphold informasjonOmUtenlandsopphold, String situasjon,
                         Boolean erEndringssøknad, String tilleggsopplysninger, List<Vedlegg> vedlegg) {
        super(opprettet, type, saksnummer, søker, barn, annenForelder, informasjonOmUtenlandsopphold, situasjon,
            erEndringssøknad, tilleggsopplysninger, vedlegg);
    }

    @Override
    public String toString() {
        return "Engangsstønad{} " + super.toString();
    }
}
