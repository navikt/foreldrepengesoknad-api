package no.nav.foreldrepenger.selvbetjening.innsyn.sakerV2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.math.BigDecimal;

record SamtidigUttak(@JsonValue BigDecimal value) {

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    SamtidigUttak {
    }

    @Override
    public BigDecimal value() {
        return value;
    }
}
