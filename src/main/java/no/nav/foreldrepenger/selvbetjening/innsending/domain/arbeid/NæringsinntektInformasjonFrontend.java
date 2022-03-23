package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.time.LocalDate;

import javax.validation.constraints.Pattern;

public record NæringsinntektInformasjonFrontend(LocalDate dato,
                                                int næringsinntektEtterEndring,
                                                @Pattern(regexp = FRITEKST) String forklaring) {
}
