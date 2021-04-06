package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.TilknyttetPerson;

record RegnskapsførerDto(String navn, String telefon) {

    static RegnskapsførerDto from(TilknyttetPerson p) {
        return new RegnskapsførerDto(p.getNavn(), p.getTelefonnummer());
    }
}