package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import java.time.LocalDate;

public record Arbeidsforhold(String arbeidsgiverId,
                             String arbeidsgiverIdType,
                             String arbeidsgiverNavn,
                             Double stillingsprosent,
                             LocalDate fom,
                             LocalDate tom) {

}
