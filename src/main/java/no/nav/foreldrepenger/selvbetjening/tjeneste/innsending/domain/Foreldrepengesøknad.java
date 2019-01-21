package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain;

import java.util.List;

public class Foreldrepengesøknad extends Søknad {

    public String dekningsgrad;
    public List<UttaksplanPeriode> uttaksplan;

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [dekningsgrad=" + dekningsgrad + ", uttaksplan=" + uttaksplan + "]";
    }
}
