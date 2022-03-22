package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import javax.validation.constraints.Pattern;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Tidsperiode;

public record FrilansoppdragFrontend(@Pattern(regexp = FRITEKST) String navnPåArbeidsgiver, Tidsperiode tidsperiode) {
}
