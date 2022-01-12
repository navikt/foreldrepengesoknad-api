package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_EMPTY)
public record Søkerinfo(PersonFrontend søker, List<Arbeidsforhold> arbeidsforhold) {

}
