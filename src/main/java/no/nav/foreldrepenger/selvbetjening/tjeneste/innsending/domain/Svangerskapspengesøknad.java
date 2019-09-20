package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.tilrettelegging.Tilrettelegging;

public class Svangerskapspengesøknad extends Søknad {
    public List<Tilrettelegging> tilrettelegging;

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [tilrettelegging=" + tilrettelegging + ", type=" + getType() + ", saksnummer="
                + getSaksnummer() + ", søker=" + getSøker() + ", opprettet=" + getOpprettet() + ", barn=" + getBarn() + ", annenForelder="
                + getAnnenForelder() + ", informasjonOmUtenlandsopphold=" + getInformasjonOmUtenlandsopphold() + ", situasjon="
                + getSituasjon() + ", erEndringssøknad=" + getErEndringssøknad() + ", tilleggsopplysninger="
                + getTilleggsopplysninger() + ", vedlegg=" + getVedlegg() + "]";
    }
}
