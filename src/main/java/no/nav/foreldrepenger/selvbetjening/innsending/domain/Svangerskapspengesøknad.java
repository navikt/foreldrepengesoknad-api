package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging.Tilrettelegging;

public final class Svangerskapspengesøknad extends Søknad {
    private List<Tilrettelegging> tilrettelegging;

    public List<Tilrettelegging> getTilrettelegging() {
        return tilrettelegging;
    }

    public void setTilrettelegging(List<Tilrettelegging> tilrettelegging) {
        this.tilrettelegging = tilrettelegging;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [tilrettelegging=" + getTilrettelegging() + ", type=" + getType()
                + ", saksnummer="
                + getSaksnummer() + ", søker=" + getSøker() + ", opprettet=" + getOpprettet() + ", barn=" + getBarn()
                + ", annenForelder="
                + getAnnenForelder() + ", informasjonOmUtenlandsopphold=" + getInformasjonOmUtenlandsopphold()
                + ", situasjon="
                + getSituasjon() + ", erEndringssøknad=" + getErEndringssøknad() + ", tilleggsopplysninger="
                + getTilleggsopplysninger() + ", vedlegg=" + getVedlegg() + "]";
    }
}
