package no.nav.foreldrepenger.selvbetjening.rest.json;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Soknad {

    public String id;
    public String fnr;

    public LocalDateTime opprettet;
    public LocalDateTime sistEndret;

    public LocalDate termindato;
    public LocalDate terminbekreftelseDato;

}
