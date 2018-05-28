package no.nav.foreldrepenger.selvbetjening.innsending.json;

import java.util.ArrayList;
import java.util.List;

public class Foreldrepengesøknad extends Søknad {

    public Barn barn;
    public AnnenForelder annenForelder;
    public Utenlandsopphold utenlandsopphold;

    public String situasjon;

    public String søkerRolle;
    public Boolean erSelvstendigNæringsdrivende;
    public Boolean erFrilanser;

    public List<UttaksplanPeriode> uttaksplan;

    public Foreldrepengesøknad() {
        this.barn = new Barn();
        this.annenForelder = new AnnenForelder();
        this.utenlandsopphold = new Utenlandsopphold();
        this.uttaksplan = new ArrayList<UttaksplanPeriode>();
    }

}
