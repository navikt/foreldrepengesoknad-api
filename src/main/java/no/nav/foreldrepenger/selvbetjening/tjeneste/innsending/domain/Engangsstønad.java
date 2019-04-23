package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain;

public class Engangsstønad extends Søknad {

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [type=" + type + ", saksnummer=" + saksnummer + ", søker=" + søker
                + ", opprettet="
                + opprettet + ", barn=" + barn + ", annenForelder=" + annenForelder + ", informasjonOmUtenlandsopphold="
                + informasjonOmUtenlandsopphold + ", situasjon=" + situasjon + ", erEndringssøknad=" + erEndringssøknad
                + ", tilleggsopplysninger=" + tilleggsopplysninger + ", vedlegg=" + vedlegg + "]";
    }

}
