package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import java.util.List;

public record Utenlandsopphold(List<UtenlandsoppholdPeriode> tidligereOpphold, List<UtenlandsoppholdPeriode> senereOpphold) {

}
