package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Data
public class Arbeidsforhold {

    private final String arbeidsgiverId;
    private final String arbeidsgiverIdType;
    private final String arbeidsgiverNavn;
    private final Double stillingsprosent;
    @JsonAlias("from")
    private final LocalDate fom;
    @JsonAlias("to")
    private final LocalDate tom;
}
