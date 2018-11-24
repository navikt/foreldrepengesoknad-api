package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonValue;

public class AktørId {

    private final String aktør;

    @NotNull
    public AktørId(String aktør) {
        this.aktør = Objects.requireNonNull(aktør);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(aktør);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        AktørId that = (AktørId) o;
        return Objects.equals(aktør, that.aktør);
    }

    @JsonValue
    public String getAktør() {
        return aktør;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [aktør=" + aktør + "]";
    }
}
