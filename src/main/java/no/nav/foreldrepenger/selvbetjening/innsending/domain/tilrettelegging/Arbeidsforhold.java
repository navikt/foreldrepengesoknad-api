package no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_BOKSTAVER;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import javax.validation.constraints.Pattern;

public record Arbeidsforhold(@Pattern(regexp = BARE_BOKSTAVER) String type,
                             @Pattern(regexp = FRITEKST) String id,
                             @Pattern(regexp = FRITEKST) String risikofaktorer,
                             @Pattern(regexp = FRITEKST) String tilretteleggingstiltak) {
}
