package no.nav.foreldrepenger.selvbetjening.rest.json;

import java.time.LocalDateTime;

public class Søknad {

    public String fnr; // TODO: Skal ikke komme fra klienten, skal hentes fra sikkerhetskonteksten

    public LocalDateTime opprettet;
    public LocalDateTime sistEndret;

}
