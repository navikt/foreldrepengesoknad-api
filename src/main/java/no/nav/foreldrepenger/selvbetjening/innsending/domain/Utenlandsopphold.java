package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import java.util.List;

public class Utenlandsopphold {
    private List<UtenlandsoppholdPeriode> tidligereOpphold;
    private List<UtenlandsoppholdPeriode> senereOpphold;

    public List<UtenlandsoppholdPeriode> getTidligereOpphold() {
        return tidligereOpphold;
    }

    public void setTidligereOpphold(List<UtenlandsoppholdPeriode> tidligereOpphold) {
        this.tidligereOpphold = tidligereOpphold;
    }

    public List<UtenlandsoppholdPeriode> getSenereOpphold() {
        return senereOpphold;
    }

    public void setSenereOpphold(List<UtenlandsoppholdPeriode> senereOpphold) {
        this.senereOpphold = senereOpphold;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[tidligereOpphold=" + tidligereOpphold + ", senereOpphold="
                + senereOpphold + "]";
    }
}
