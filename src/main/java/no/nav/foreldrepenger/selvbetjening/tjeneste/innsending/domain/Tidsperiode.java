package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_NULL)
public class Tidsperiode {
    public LocalDate fom;
    public LocalDate tom;

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [fom=" + fom + ", tom=" + tom + "]";
    }
}
