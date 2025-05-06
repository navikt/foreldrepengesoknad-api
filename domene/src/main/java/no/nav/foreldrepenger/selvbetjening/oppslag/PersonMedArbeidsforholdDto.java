package no.nav.foreldrepenger.selvbetjening.oppslag;

import no.nav.foreldrepenger.common.domain.felles.Person;
import no.nav.foreldrepenger.selvbetjening.oppslag.dto.Arbeidsforhold;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public record PersonMedArbeidsforholdDto(Person person, List<Arbeidsforhold> arbeidsforhold) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PersonMedArbeidsforholdDto that = (PersonMedArbeidsforholdDto) o;
        return Objects.equals(person, that.person) && likArbeidsforhold(that);
    }

    private boolean likArbeidsforhold(PersonMedArbeidsforholdDto that) {
        if (Objects.equals(arbeidsforhold, that.arbeidsforhold)) return true;
        if (arbeidsforhold == null || that.arbeidsforhold == null) return false;
        if (arbeidsforhold.size() != that.arbeidsforhold.size()) return false;

        return arbeidsforhold.stream().allMatch(b ->
                Collections.frequency(arbeidsforhold, b) == Collections.frequency(that.arbeidsforhold, b)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, arbeidsforhold);
    }
}
