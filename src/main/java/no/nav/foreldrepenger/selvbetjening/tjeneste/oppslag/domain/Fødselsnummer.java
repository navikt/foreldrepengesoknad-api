package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;

public class Fødselsnummer {

    private final String fnr;

    public Fødselsnummer(String fnr) {
        this.fnr = fnr;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fnr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        Fødselsnummer that = (Fødselsnummer) o;
        return Objects.equals(fnr, that.fnr);
    }

    @JsonValue
    public String getFnr() {
        return fnr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [fnr=" + fnr + "]";
    }
}
