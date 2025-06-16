package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

public record AvtaltFerieDto(@Valid @NotNull ArbeidsforholdDto arbeidsforhold, @NotNull LocalDate fom, @NotNull LocalDate tom) {

    @AssertTrue(message = "Kan bare legge til avtalt ferie for virksomhet")
    public boolean isArbeidsforholdVirksomhet() {
        return arbeidsforhold instanceof ArbeidsforholdDto.VirksomhetDto || arbeidsforhold instanceof ArbeidsforholdDto.PrivatArbeidsgiverDto;
    }
}
