package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

public record TilknyttetPerson(String navn,
                               String telefonnummer,
                               boolean erNærVennEllerFamilie) {
}
