package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger;

import jakarta.validation.Valid;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.ArbeidsforholdDto;

import java.time.LocalDate;

public record AvtaltFerie(@Valid ArbeidsforholdDto arbeidsforholdDto, LocalDate fom, LocalDate tom) {
}
