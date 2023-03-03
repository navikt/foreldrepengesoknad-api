package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

public record JournalpostId(@JsonValue @NotNull @Digits(integer = 18, fraction = 0) String value) {

    @JsonCreator
    public static JournalpostId valueOf(String id) {
        return new JournalpostId(id);
    }

    @Override
    public String value() {
        return value;
    }
}
