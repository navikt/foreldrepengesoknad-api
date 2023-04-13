package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Tidsperiode;

public record FrilansoppdragFrontend(@Pattern(regexp = FRITEKST) String navnPåArbeidsgiver, @Valid Tidsperiode tidsperiode) {
}
