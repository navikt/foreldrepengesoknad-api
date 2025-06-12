package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UtenlandsoppholdDtoOLD(@Valid @Size(max = 20) List<@Valid @NotNull UtenlandsoppholdPeriodeDtoOLD> tidligereOpphold,
                                     @Valid @Size(max = 20) List<@Valid @NotNull UtenlandsoppholdPeriodeDtoOLD> senereOpphold) {
}
