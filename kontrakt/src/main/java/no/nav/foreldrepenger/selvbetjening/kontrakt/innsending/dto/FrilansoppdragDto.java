package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

public record FrilansoppdragDto(@Pattern(regexp = FRITEKST) String navnPåArbeidsgiver,
                                @Valid ÅpenPeriodeDto tidsperiode) {
}
