package no.nav.foreldrepenger.selvbetjening.innsending.dto.svangerskapspenger;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.validation.Valid;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.MutableVedleggReferanseDto;

public record TilretteleggingDto(Type type,
                                 @Valid ArbeidsforholdDto arbeidsforhold,
                                 Double stillingsprosent,
                                 LocalDate behovForTilretteleggingFom,
                                 LocalDate tilrettelagtArbeidFom,
                                 LocalDate slutteArbeidFom,
                                 List<MutableVedleggReferanseDto> vedlegg) {
    public enum Type {
        HEL("hel"),
        DELVIS("delvis"),
        INGEN("ingen");

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
