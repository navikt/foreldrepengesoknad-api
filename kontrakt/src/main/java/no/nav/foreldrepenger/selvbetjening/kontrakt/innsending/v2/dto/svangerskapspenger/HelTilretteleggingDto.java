package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.ArbeidsforholdDto;

public record HelTilretteleggingDto(@Valid @NotNull ArbeidsforholdDto arbeidsforhold,
                                    @NotNull LocalDate behovForTilretteleggingFom,
                                    @NotNull LocalDate tilrettelagtArbeidFom,
                                    @Valid @Size(max = 30) List<@Valid @NotNull MutableVedleggReferanseDto> vedleggsreferanser) implements TilretteleggingDto {
}
