package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;

public record TilretteleggingDto(Type type,
                                 @Valid ArbeidsforholdDto arbeidsforhold,
                                 @Min(0) @Max(100) Double stillingsprosent,
                                 LocalDate behovForTilretteleggingFom,
                                 LocalDate tilrettelagtArbeidFom,
                                 LocalDate slutteArbeidFom,
                                 @Valid @Size(max = 15) List<@Valid MutableVedleggReferanseDto> vedlegg) {

    public TilretteleggingDto {
        vedlegg = Optional.ofNullable(vedlegg).orElse(List.of());
    }

    public enum Type {
        HEL,
        DELVIS,
        INGEN
    }
}
