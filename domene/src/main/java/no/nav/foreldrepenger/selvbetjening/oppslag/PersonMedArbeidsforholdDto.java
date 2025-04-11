package no.nav.foreldrepenger.selvbetjening.oppslag;

import no.nav.foreldrepenger.common.domain.felles.Person;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Arbeidsforhold;

import java.util.List;
import java.util.Objects;

public record PersonMedArbeidsforholdDto(Person person, List<Arbeidsforhold> arbeidsforhold) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PersonMedArbeidsforholdDto that = (PersonMedArbeidsforholdDto) o;
        return Objects.equals(person, that.person) && LikArbeidsforhold(that);
    }

    private boolean LikArbeidsforhold(PersonMedArbeidsforholdDto that) {
        if (arbeidsforhold == null && that.arbeidsforhold == null) {
            return true;
        }

        if (arbeidsforhold == null || that.arbeidsforhold == null) {
            return false;
        }

        if (arbeidsforhold.size() != that.arbeidsforhold.size()) {
            return false;
        }

        return arbeidsforhold.containsAll(that.arbeidsforhold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, arbeidsforhold);
    }
}
