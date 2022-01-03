package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.EqualsAndHashCode;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging.Tilrettelegging;

@EqualsAndHashCode(callSuper = true)
public final class Svangerskapspengesøknad extends Søknad {
    private final List<Tilrettelegging> tilrettelegging;

    @JsonCreator
    public Svangerskapspengesøknad(LocalDateTime opprettet, String type, String saksnummer, Søker søker, Barn barn,
                                   AnnenForelder annenForelder, Utenlandsopphold informasjonOmUtenlandsopphold,
                                   String situasjon, Boolean erEndringssøknad, String tilleggsopplysninger,
                                   List<Vedlegg> vedlegg, List<Tilrettelegging> tilrettelegging) {
        super(opprettet, type, saksnummer, søker, barn, annenForelder, informasjonOmUtenlandsopphold, situasjon,
            erEndringssøknad, tilleggsopplysninger, vedlegg);
        this.tilrettelegging = tilrettelegging;
    }

    public List<Tilrettelegging> getTilrettelegging() {
        return tilrettelegging;
    }

    @Override
    public String toString() {
        return "Svangerskapspengesøknad{" +
            "tilrettelegging=" + tilrettelegging +
            "} " + super.toString();
    }
}
