package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain;

import java.time.LocalDate;

public class Arbeidsforhold {
    public String arbeidsgiverId;
    public String arbeidsgiverIdType;
    public String arbeidsgiverNavn;
    public Double stillingsprosent;
    public LocalDate fom;
    public LocalDate tom;
}
