package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TilretteleggingbehovDto(@Valid @NotNull ArbeidsforholdDto arbeidsforhold,
                                      @NotNull LocalDate behovForTilretteleggingFom,
                                      @Pattern(regexp = FRITEKST) String risikofaktorer,
                                      @Pattern(regexp = FRITEKST) String tilretteleggingstiltak,
                                      @Valid @Size(min = 1, max = 20) List<@Valid @NotNull TilretteleggingDto> tilrettelegginger) {

    @AssertTrue(message = "Tilrettelegging av næring eller frilans må ha satt risikofaktorer og tilretteleggingstiltak")
    public boolean isRisikofaktorerOgTilretteleggingtiltakSattForNæringFrilans() {
        if (arbeidsforhold instanceof ArbeidsforholdDto.FrilanserDto || arbeidsforhold instanceof ArbeidsforholdDto.SelvstendigNæringsdrivendeDto) {
            return risikofaktorer != null && tilretteleggingstiltak != null;
        }
        return true;
    }

    @JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = TilretteleggingDto.Hel.class, name = "hel"),
        @JsonSubTypes.Type(value = TilretteleggingDto.Del.class, name = "delvis"),
        @JsonSubTypes.Type(value = TilretteleggingDto.Ingen.class, name = "ingen")})
    public interface TilretteleggingDto {
        LocalDate fom();

        record Hel(@NotNull LocalDate fom) implements TilretteleggingDto {
        }

        record Del(@NotNull LocalDate fom, @NotNull @Min(0) @Max(100) Double stillingsprosent) implements TilretteleggingDto {
        }

        record Ingen(@NotNull LocalDate fom) implements TilretteleggingDto {
        }
    }
}
