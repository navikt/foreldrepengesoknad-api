package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record BrukerTekst(@NotNull @Pattern(regexp = FRITEKST) String dokumentType,
                          @Pattern(regexp = FRITEKST) String tekst,
                          @Pattern(regexp = FRITEKST) String overskrift) {
}
