package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import java.util.List;

public class Foreldrepengesøknad extends Søknad {

    private String dekningsgrad;
    private List<UttaksplanPeriode> uttaksplan;

    public String getDekningsgrad() {
        return dekningsgrad;
    }

    public void setDekningsgrad(String dekningsgrad) {
        this.dekningsgrad = dekningsgrad;
    }

    public List<UttaksplanPeriode> getUttaksplan() {
        return uttaksplan;
    }

    public void setUttaksplan(List<UttaksplanPeriode> uttaksplan) {
        this.uttaksplan = uttaksplan;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [dekningsgrad=" + getDekningsgrad() + ", uttaksplan=" + getUttaksplan()
                + "]";
    }
}
