package no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

public record AnnenPart(AktørId aktørId) {

    @JsonCreator
    public AnnenPart {
        Objects.requireNonNull(aktørId, "aktørId må være non-null");
    }
}
