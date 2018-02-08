package no.nav.foreldrepenger.selvbetjening.consumer.json;

import no.nav.foreldrepenger.selvbetjening.rest.json.Engangsstonad;

import java.time.LocalDate;

public class EngangsstonadDto {

    public LocalDate motattdato;

    public EngangsstonadDto() {}

    public EngangsstonadDto(Engangsstonad engangsstonad) {
        this.motattdato = LocalDate.now();
    }
}
