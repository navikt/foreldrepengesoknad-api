package no.nav.foreldrepenger.selvbetjening.innsyn.sakerV2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public record AktørId(@JsonValue String value) {

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public AktørId {
        Objects.requireNonNull(value, "AktørId kan ikke være null");
    }

    @Override
    public String value() {
        return value;
    }
}
