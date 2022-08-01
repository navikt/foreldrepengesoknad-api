package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_BOKSTAVER;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

public record UtenlandsoppholdPeriodeFrontend(@Pattern(regexp = BARE_BOKSTAVER) String land, @Valid Tidsperiode tidsperiode) {

}
