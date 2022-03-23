package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public record BrukerTekst(@NotNull @Pattern(regexp = FRITEKST) String dokumentType,
                          @Pattern(regexp = FRITEKST) String tekst,
                          @Pattern(regexp = FRITEKST) String overskrift) {
}
