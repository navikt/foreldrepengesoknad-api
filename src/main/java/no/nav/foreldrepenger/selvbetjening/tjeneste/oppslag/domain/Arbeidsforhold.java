package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain;

import java.time.LocalDate;

public class Arbeidsforhold {

    public String arbeidsgiverId;
    public String arbeidsgiverIdType;
    public String arbeidsgiverNavn;
    public Double stillingsprosent;
    public LocalDate fom;
    public LocalDate tom;

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [arbeidsgiverId=" + arbeidsgiverId + ", arbeidsgiverIdType="
                + arbeidsgiverIdType
                + ", arbeidsgiverNavn=" + arbeidsgiverNavn + ", stillingsprosent=" + stillingsprosent + ", fom=" + fom
                + ", tom=" + tom + "]";
    }
}
