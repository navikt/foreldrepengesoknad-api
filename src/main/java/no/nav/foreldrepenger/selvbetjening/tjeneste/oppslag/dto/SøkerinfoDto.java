package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.dto;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Arbeidsforhold;

public class SøkerinfoDto {
    public PersonDto person;
    public List<Arbeidsforhold> arbeidsforhold;
}
