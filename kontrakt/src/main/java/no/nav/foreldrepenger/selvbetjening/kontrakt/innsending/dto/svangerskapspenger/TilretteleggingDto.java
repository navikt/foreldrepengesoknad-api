package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;

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
        HEL,
        DELVIS,
        INGEN
    }
}
