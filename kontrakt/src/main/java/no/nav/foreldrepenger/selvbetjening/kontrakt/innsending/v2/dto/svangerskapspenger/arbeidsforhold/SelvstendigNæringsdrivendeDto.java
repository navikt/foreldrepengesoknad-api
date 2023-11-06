package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import jakarta.validation.constraints.Pattern;

public record SelvstendigNæringsdrivendeDto(@Pattern(regexp = FRITEKST) String risikofaktorer,
                                            @Pattern(regexp = FRITEKST) String tilretteleggingstiltak) implements ArbeidsforholdDto {
}
