package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import jakarta.validation.constraints.Pattern;

public record ArbeidsforholdDto(Type type,
                                @Pattern(regexp = FRITEKST) String id,
                                @Pattern(regexp = FRITEKST) String risikofaktorer,
                                @Pattern(regexp = FRITEKST) String tilretteleggingstiltak) {
    public enum Type {
        VIRKSOMHET,
        PRIVAT,
        SELVSTENDIG,
        FRILANSER
    }
}
