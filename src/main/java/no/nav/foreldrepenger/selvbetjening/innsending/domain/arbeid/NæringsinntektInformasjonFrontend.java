package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.time.LocalDate;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;

public record NæringsinntektInformasjonFrontend(LocalDate dato,
                                                @Digits(integer = 9, fraction = 0) int næringsinntektEtterEndring,
                                                @Pattern(regexp = FRITEKST) String forklaring) {
}
