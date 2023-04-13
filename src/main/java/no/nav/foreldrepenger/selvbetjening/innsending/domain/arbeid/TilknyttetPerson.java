package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import jakarta.validation.constraints.Pattern;

public record TilknyttetPerson(@Pattern(regexp = FRITEKST) String navn,
                               @Pattern(regexp = FRITEKST) String telefonnummer,
                               boolean erNærVennEllerFamilie) {
}
