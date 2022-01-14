package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import java.time.LocalDate;

public record NæringsinntektInformasjonFrontend(LocalDate dato,
                                                int næringsinntektEtterEndring,
                                                String forklaring) {
}
