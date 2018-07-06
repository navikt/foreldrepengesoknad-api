package no.nav.foreldrepenger.selvbetjening.innsending.json;

import java.util.List;

public class Foreldrepengesøknad extends Søknad {

    // TODO: Flytt disse til Søknad
    public Barn barn;
    public AnnenForelder annenForelder;
    public Utenlandsopphold informasjonOmUtenlandsopphold;

    public String situasjon;

    public List<UttaksplanPeriode> uttaksplan;



}
