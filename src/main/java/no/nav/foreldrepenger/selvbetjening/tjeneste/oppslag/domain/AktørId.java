package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonValue;

public class AktørId {

    private final String aktørId;

    @NotNull
    public AktørId(String aktør) {
        this.aktørId = Objects.requireNonNull(aktør);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(aktørId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AktørId that = (AktørId) o;
        return Objects.equals(aktørId, that.aktørId);
    }

    @JsonValue
    public String getAktørId() {
        return aktørId;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [aktørId=" + aktørId + "]";
    }
}
