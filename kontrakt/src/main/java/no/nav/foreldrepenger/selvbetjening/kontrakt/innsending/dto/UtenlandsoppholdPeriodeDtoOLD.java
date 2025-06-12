package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_BOKSTAVER;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

public record UtenlandsoppholdPeriodeDtoOLD(@Pattern(regexp = BARE_BOKSTAVER) String land, @Valid ÅpenPeriodeDtoOLD tidsperiode) {
}
