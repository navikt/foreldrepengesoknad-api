package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain;

import java.util.List;

public class Utenlandsopphold {
    public List<UtenlandsoppholdPeriode> tidligereOpphold;
    public List<UtenlandsoppholdPeriode> senereOpphold;

    @Override
    public String toString() {
        return "Utenlandsopphold{" +
                "tidligereOpphold=" + tidligereOpphold +
                ", senereOpphold=" + senereOpphold +
                '}';
    }
}
