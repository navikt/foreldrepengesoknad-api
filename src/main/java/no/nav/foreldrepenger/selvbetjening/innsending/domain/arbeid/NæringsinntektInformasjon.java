package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import java.time.LocalDate;

public record NæringsinntektInformasjon(LocalDate dato, Integer næringsinntektEtterEndring, String forklaring) {

}
