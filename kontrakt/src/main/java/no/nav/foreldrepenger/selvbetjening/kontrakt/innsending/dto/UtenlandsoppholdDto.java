package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UtenlandsoppholdDto(@Valid @Size(max = 20) List<@Valid @NotNull UtenlandsoppholdPeriodeDto> tidligereOpphold,
                                  @Valid @Size(max = 20) List<@Valid @NotNull UtenlandsoppholdPeriodeDto> senereOpphold) {
}
