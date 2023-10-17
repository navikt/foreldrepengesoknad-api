package no.nav.foreldrepenger.selvbetjening.innsending.domain.svangerskapspenger;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.validation.Valid;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.MutableVedleggReferanseDto;

public record TilretteleggingDto(Type type,
                                 @Valid ArbeidsforholdDto arbeidsforhold,
                                 Double stillingsprosent,
                                 LocalDate behovForTilretteleggingFom,
                                 LocalDate tilrettelagtArbeidFom,
                                 LocalDate slutteArbeidFom,
                                 List<@Valid MutableVedleggReferanseDto> vedlegg) {

    public TilretteleggingDto {
        vedlegg = Optional.ofNullable(vedlegg).orElse(List.of());
    }

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
