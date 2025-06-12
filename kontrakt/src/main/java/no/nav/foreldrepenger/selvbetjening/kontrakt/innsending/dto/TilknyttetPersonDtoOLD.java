package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import jakarta.validation.constraints.Pattern;

public record TilknyttetPersonDtoOLD(@Pattern(regexp = FRITEKST) String navn, @Pattern(regexp = FRITEKST) String telefonnummer,
                                     boolean erNærVennEllerFamilie) {
}
