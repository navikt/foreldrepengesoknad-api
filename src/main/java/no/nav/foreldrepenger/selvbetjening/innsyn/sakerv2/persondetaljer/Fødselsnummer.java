package no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2.persondetaljer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public record Fødselsnummer(@JsonValue String value) {

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public Fødselsnummer {
        Objects.requireNonNull(value, "Fødselsnummer kan ikke være null");
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return "************";
    }
}
