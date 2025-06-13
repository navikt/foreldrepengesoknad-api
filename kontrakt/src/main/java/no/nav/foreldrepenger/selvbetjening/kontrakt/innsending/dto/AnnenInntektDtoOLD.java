package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_BOKSTAVER;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

public record AnnenInntektDtoOLD(@Pattern(regexp = "^[\\p{L}_]*$") String type,
                                 @Pattern(regexp = BARE_BOKSTAVER) String land,
                                 @Pattern(regexp = FRITEKST) String arbeidsgiverNavn,
                                 @Valid ÅpenPeriodeDtoOLD tidsperiode,
                                 boolean erNærVennEllerFamilieMedArbeisdgiver) {
}
