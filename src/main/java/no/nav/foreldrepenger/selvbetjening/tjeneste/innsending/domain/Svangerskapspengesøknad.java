package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.tilrettelegging.Tilrettelegging;

public class Svangerskapspengesøknad extends Søknad {
    public List<Tilrettelegging> tilrettelegging;

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [tilrettelegging=" + tilrettelegging + ", type=" + type + ", saksnummer="
                + saksnummer + ", søker=" + søker + ", opprettet=" + opprettet + ", barn=" + barn + ", annenForelder="
                + annenForelder + ", informasjonOmUtenlandsopphold=" + informasjonOmUtenlandsopphold + ", situasjon="
                + situasjon + ", erEndringssøknad=" + erEndringssøknad + ", tilleggsopplysninger="
                + tilleggsopplysninger + ", vedlegg=" + vedlegg + "]";
    }
}
