package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;

public record UtenlandsoppholdFrontend(@Valid @Size(max = 20) List<UtenlandsoppholdPeriodeFrontend> tidligereOpphold,
                                       @Valid @Size(max = 20) List<UtenlandsoppholdPeriodeFrontend> senereOpphold){

    @JsonCreator
    public UtenlandsoppholdFrontend(List<UtenlandsoppholdPeriodeFrontend> tidligereOpphold, List<UtenlandsoppholdPeriodeFrontend> senereOpphold) {
        this.tidligereOpphold = Optional.ofNullable(tidligereOpphold).orElse(emptyList());
        this.senereOpphold = Optional.ofNullable(senereOpphold).orElse(emptyList());
    }
}
