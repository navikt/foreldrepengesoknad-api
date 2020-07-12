package no.nav.foreldrepenger.selvbetjening.oppslag.dto;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Arbeidsforhold;

public class SøkerinfoDto {
    public PersonDto person;
    public List<Arbeidsforhold> arbeidsforhold;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[person=" + person + ", arbeidsforhold=" + arbeidsforhold + "]";
    }
}
