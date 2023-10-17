package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

public record UtenlandsoppholdDto(@Valid @Size(max = 20) List<UtenlandsoppholdPeriodeDto> tidligereOpphold,
                                  @Valid @Size(max = 20) List<UtenlandsoppholdPeriodeDto> senereOpphold) {
}
