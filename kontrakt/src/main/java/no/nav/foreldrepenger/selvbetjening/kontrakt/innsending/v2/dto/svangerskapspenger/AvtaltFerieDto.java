package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger;

import java.time.LocalDate;

import jakarta.validation.Valid;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.ArbeidsforholdDto;

public record AvtaltFerieDto(@Valid ArbeidsforholdDto arbeidsforhold, LocalDate fom, LocalDate tom) {
}
