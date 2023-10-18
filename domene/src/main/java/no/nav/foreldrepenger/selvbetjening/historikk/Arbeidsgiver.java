package no.nav.foreldrepenger.selvbetjening.historikk;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = ANY)

public record Arbeidsgiver(String navn, String orgnr) {

}
