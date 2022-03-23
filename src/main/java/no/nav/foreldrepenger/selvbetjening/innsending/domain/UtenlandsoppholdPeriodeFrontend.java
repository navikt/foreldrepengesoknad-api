package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import javax.validation.constraints.Pattern;

public record UtenlandsoppholdPeriodeFrontend(@Pattern(regexp = FRITEKST) String land, Tidsperiode tidsperiode) {

}
