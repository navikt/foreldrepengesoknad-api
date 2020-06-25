package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Arbeidsforhold {

    public String arbeidsgiverId;
    public String arbeidsgiverIdType;
    public String arbeidsgiverNavn;
    public Double stillingsprosent;
    @JsonAlias("from")
    public LocalDate fom;
    @JsonAlias("to")
    public LocalDate tom;

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [arbeidsgiverId=" + arbeidsgiverId + ", arbeidsgiverIdType="
                + arbeidsgiverIdType
                + ", arbeidsgiverNavn=" + arbeidsgiverNavn + ", stillingsprosent=" + stillingsprosent + ", fom=" + fom
                + ", tom=" + tom + "]";
    }
}
