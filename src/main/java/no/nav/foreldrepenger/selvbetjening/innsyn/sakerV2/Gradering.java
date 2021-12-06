package no.nav.foreldrepenger.selvbetjening.innsyn.sakerV2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.math.BigDecimal;

record Gradering(Arbeidstidprosent arbeidstidprosent) {

    @JsonCreator
    Gradering(BigDecimal arbeidstidprosent) {
        this(new Arbeidstidprosent(arbeidstidprosent));
    }

    static record Arbeidstidprosent(@JsonValue BigDecimal value) {
        @Override
        public BigDecimal value() {
            return value;
        }
    }
}
