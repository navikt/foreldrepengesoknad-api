package no.nav.foreldrepenger.selvbetjening.innsending.json;

import java.util.List;

public class Utenlandsopphold {

    public Boolean fødselINorge;

    public Boolean iNorgeNeste12Mnd;
    public Boolean iNorgeSiste12Mnd;
    public Boolean jobbetINorgeSiste12Mnd;

    public List<UtenlandsoppholdPeriode> tidligereOpphold;
    public List<UtenlandsoppholdPeriode> senereOpphold;

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [fødselINorge=" + fødselINorge + ", iNorgeNeste12Mnd=" + iNorgeNeste12Mnd
                + ", iNorgeSiste12Mnd=" + iNorgeSiste12Mnd + ", jobbetINorgeSiste12Mnd=" + jobbetINorgeSiste12Mnd
                + ", tidligereOpphold=" + tidligereOpphold + ", senereOpphold=" + senereOpphold + "]";
    }

}
