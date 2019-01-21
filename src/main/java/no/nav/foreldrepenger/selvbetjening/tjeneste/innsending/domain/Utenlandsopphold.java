package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain;

import java.util.List;

public class Utenlandsopphold {

    public Boolean iNorgePåHendelsestidspunktet;
    public Boolean iNorgeNeste12Mnd;
    public Boolean iNorgeSiste12Mnd;

    public List<UtenlandsoppholdPeriode> tidligereOpphold;
    public List<UtenlandsoppholdPeriode> senereOpphold;

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [iNorgePåHendelsestidspunktet=" + iNorgePåHendelsestidspunktet
                + ", iNorgeNeste12Mnd="
                + iNorgeNeste12Mnd + ", iNorgeSiste12Mnd=" + iNorgeSiste12Mnd + ", tidligereOpphold=" + tidligereOpphold
                + ", senereOpphold=" + senereOpphold + "]";
    }

}
