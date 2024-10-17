package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.tilrettelegging;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.ArbeidsforholdDto;

public record DelvisTilretteleggingDto(@Valid @NotNull ArbeidsforholdDto arbeidsforhold,
                                       @NotNull LocalDate behovForTilretteleggingFom,
                                       @NotNull LocalDate tilrettelagtArbeidFom,
                                       @NotNull @Min(0) @Max(100) Double stillingsprosent) implements TilretteleggingDto {
}
