package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;

public record Utenlandsopphold(List<UtenlandsoppholdPeriode> tidligereOpphold,
                               List<UtenlandsoppholdPeriode> senereOpphold){

    @JsonCreator
    public Utenlandsopphold(List<UtenlandsoppholdPeriode> tidligereOpphold, List<UtenlandsoppholdPeriode> senereOpphold) {
        this.tidligereOpphold = Optional.ofNullable(tidligereOpphold).orElse(emptyList());
        this.senereOpphold = Optional.ofNullable(senereOpphold).orElse(emptyList());
    }
}
