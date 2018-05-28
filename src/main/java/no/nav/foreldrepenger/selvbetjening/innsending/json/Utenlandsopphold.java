package no.nav.foreldrepenger.selvbetjening.innsending.json;

import java.util.List;

public class Utenlandsopphold {

    public Boolean fødselINorge;
    public Boolean iNorgeNeste12Mnd;
    public Boolean iNorgeSiste12Mnd;
    public Boolean jobbetINorgeSiste12Mnd;

    public List<UtenlandsoppholdPeriode> tidligerePerioder;
    public List<UtenlandsoppholdPeriode> senerePerioder;

}
