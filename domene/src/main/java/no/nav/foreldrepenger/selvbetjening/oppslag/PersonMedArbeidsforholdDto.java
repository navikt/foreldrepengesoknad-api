package no.nav.foreldrepenger.selvbetjening.oppslag;

import no.nav.foreldrepenger.common.domain.felles.Person;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Arbeidsforhold;

import java.util.List;

public record PersonMedArbeidsforholdDto(Person person, List<Arbeidsforhold> arbeidsforhold) {
}
