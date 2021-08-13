package no.nav.foreldrepenger.selvbetjening.innsending.domain;

public final class Engangsstønad extends Søknad {

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [type=" + getType() + ", saksnummer=" + getSaksnummer() + ", søker="
                + getSøker()
                + ", opprettet="
                + getOpprettet() + ", barn=" + getBarn() + ", annenForelder=" + getAnnenForelder()
                + ", informasjonOmUtenlandsopphold="
                + getInformasjonOmUtenlandsopphold() + ", situasjon=" + getSituasjon() + ", erEndringssøknad="
                + getErEndringssøknad()
                + ", tilleggsopplysninger=" + getTilleggsopplysninger() + ", vedlegg=" + getVedlegg() + "]";
    }

}
