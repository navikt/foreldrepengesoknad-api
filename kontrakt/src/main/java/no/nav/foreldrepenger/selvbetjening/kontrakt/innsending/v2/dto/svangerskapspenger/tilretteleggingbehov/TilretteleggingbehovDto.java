package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.tilretteleggingbehov;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.ArbeidsforholdDto;

public record TilretteleggingbehovDto(@Valid @NotNull ArbeidsforholdDto arbeidsforhold,
                                      @NotNull LocalDate behovForTilretteleggingFom,
                                      @Size(max = 20) List<TilretteleggingDto> tilrettelegginger) {

    @JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = HelTilretteleggingDto.class, name = "hel"),
        @JsonSubTypes.Type(value = DelvisTilretteleggingDto.class, name = "delvis"),
        @JsonSubTypes.Type(value = IngenTilretteleggingDto.class, name = "ingen")
    })
    public interface TilretteleggingDto {
    }

    public record HelTilretteleggingDto(@NotNull LocalDate tilrettelagtArbeidFom) implements TilretteleggingDto {
    }
    public record DelvisTilretteleggingDto(@NotNull LocalDate tilrettelagtArbeidFom, @NotNull @Min(0) @Max(100) Double stillingsprosent) implements TilretteleggingDto {
    }
    public record IngenTilretteleggingDto(@NotNull LocalDate slutteArbeidFom) implements TilretteleggingDto {
    }
}
