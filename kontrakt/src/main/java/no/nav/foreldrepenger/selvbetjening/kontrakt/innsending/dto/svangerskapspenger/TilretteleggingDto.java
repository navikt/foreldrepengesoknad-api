package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record TilretteleggingDto(Type type,
                                 @Valid ArbeidsforholdDto arbeidsforhold,
                                 @Min(0) @Max(100) Double stillingsprosent,
                                 LocalDate behovForTilretteleggingFom,
                                 LocalDate tilrettelagtArbeidFom,
                                 LocalDate slutteArbeidFom) {
    public enum Type {
        HEL,
        DELVIS,
        INGEN
    }
}
