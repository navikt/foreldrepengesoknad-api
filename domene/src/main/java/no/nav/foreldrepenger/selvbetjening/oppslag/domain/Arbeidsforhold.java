package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonAlias;

public record Arbeidsforhold(String arbeidsgiverId,
                             String arbeidsgiverIdType,
                             String arbeidsgiverNavn,
                             Double stillingsprosent,
                             @JsonAlias("from") LocalDate fom,
                             @JsonAlias("to") LocalDate tom) {

}
