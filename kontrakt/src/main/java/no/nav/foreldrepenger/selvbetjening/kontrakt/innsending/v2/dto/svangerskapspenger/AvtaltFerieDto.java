package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger;

import jakarta.validation.Valid;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.ArbeidsforholdDto;

import java.time.LocalDate;

public record AvtaltFerieDto(@Valid ArbeidsforholdDto arbeidsforhold, LocalDate fom, LocalDate tom) {
}
