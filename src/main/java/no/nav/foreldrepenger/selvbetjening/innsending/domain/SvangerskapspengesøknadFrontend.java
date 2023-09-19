package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

import jakarta.validation.Valid;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging.Tilrettelegging;


public final class SvangerskapspengesøknadFrontend extends SøknadFrontend {
    @Valid
    private final List<Tilrettelegging> tilrettelegging;

    @JsonCreator
    public SvangerskapspengesøknadFrontend(String type, Saksnummer saksnummer, SøkerFrontend søker, BarnFrontend barn,
                                           AnnenForelderFrontend annenForelder, UtenlandsoppholdFrontend informasjonOmUtenlandsopphold,
                                           String situasjon, Boolean erEndringssøknad, String tilleggsopplysninger,
                                           List<VedleggFrontend> vedlegg, List<Tilrettelegging> tilrettelegging) {
        super(type, saksnummer, søker, barn, annenForelder, informasjonOmUtenlandsopphold, situasjon,
            erEndringssøknad, tilleggsopplysninger, vedlegg);
        this.tilrettelegging = tilrettelegging;
    }

    public List<Tilrettelegging> getTilrettelegging() {
        return tilrettelegging;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        var that = (SvangerskapspengesøknadFrontend) o;
        return Objects.equals(tilrettelegging, that.tilrettelegging);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tilrettelegging);
    }

    @Override
    public String toString() {
        return "Svangerskapspengesøknad{" +
            "tilrettelegging=" + tilrettelegging +
            "} " + super.toString();
    }
}
