package no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

record Saksnummer(@JsonValue String value) {

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    Saksnummer {
        Objects.requireNonNull(value, "saksnummer kan ikke være null");
    }

    @Override
    public String value() {
        return value;
    }
}
