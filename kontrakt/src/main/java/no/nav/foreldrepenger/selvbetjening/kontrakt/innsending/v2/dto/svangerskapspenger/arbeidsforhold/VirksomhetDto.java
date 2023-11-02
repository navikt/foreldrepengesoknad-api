package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.common.domain.Orgnummer;

public record VirksomhetDto(@Valid @NotNull Orgnummer id) implements ArbeidsforholdDto {
}
