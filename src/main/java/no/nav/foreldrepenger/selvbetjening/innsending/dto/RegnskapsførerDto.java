package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.TilknyttetPerson;

public class RegnskapsførerDto {
    @Override
    public String toString() {
        return "RegnskapsførerDto [navn=" + navn + ", telefon=" + telefon + "]";
    }

    public String navn;
    public String telefon;

    public RegnskapsførerDto(TilknyttetPerson person) {
        this.navn = person.getNavn();
        this.telefon = person.getTelefonnummer();
    }
}