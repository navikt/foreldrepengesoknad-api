package no.nav.foreldrepenger.selvbetjening.innsending.domain.svangerskapspenger;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.validation.constraints.Pattern;

public record ArbeidsforholdDto(Type type,
                                @Pattern(regexp = FRITEKST) String id,
                                @Pattern(regexp = FRITEKST) String risikofaktorer,
                                @Pattern(regexp = FRITEKST) String tilretteleggingstiltak) {
    public enum Type {
        VIRKSOMHET("virksomhet"),
        PRIVAT("privat"),
        SELVSTENDIG("selvstendig"),
        FRILANSER("frilanser");

        private final String verdi;

        Type(String verdi) {
            this.verdi = verdi;
        }

        @JsonValue
        public String verdi() {
            return verdi;
        }
    }
}
