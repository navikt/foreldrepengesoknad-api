package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;

public record UtenlandsoppholdFrontend(List<UtenlandsoppholdPeriodeFrontend> tidligereOpphold,
                                       List<UtenlandsoppholdPeriodeFrontend> senereOpphold){

    @JsonCreator
    public UtenlandsoppholdFrontend(List<UtenlandsoppholdPeriodeFrontend> tidligereOpphold, List<UtenlandsoppholdPeriodeFrontend> senereOpphold) {
        this.tidligereOpphold = Optional.ofNullable(tidligereOpphold).orElse(emptyList());
        this.senereOpphold = Optional.ofNullable(senereOpphold).orElse(emptyList());
    }
}
