package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_NULL)
public record Tidsperiode(LocalDate fom, LocalDate tom) {
}
