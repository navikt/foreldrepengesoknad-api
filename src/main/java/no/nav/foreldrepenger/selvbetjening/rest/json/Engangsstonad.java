package no.nav.foreldrepenger.selvbetjening.rest.json;

import java.time.LocalDate;

public class Engangsstonad extends Soknad {

    public Boolean erBarnetFodt;
    public String antallBarn;
    public LocalDate fodselsdato;

    public Boolean boddINorgeSisteAar;
    public Boolean jobbetINorgeSisteTolvMnd;
    public Boolean skalBoINorgeNesteTolvMnd;
    public Boolean skalFodeINorge;

    public static Engangsstonad stub() {
        Engangsstonad engangsstonad = new Engangsstonad();
        engangsstonad.fnr = "01018900000";
        engangsstonad.opprettet = LocalDate.of(2018, 01, 15);
        engangsstonad.termindato = LocalDate.of(2018, 01, 31);
        engangsstonad.terminbekreftelseDato = LocalDate.of(2017, 11, 24);
        engangsstonad.erBarnetFodt = false;
        engangsstonad.antallBarn = "ett";
        engangsstonad.boddINorgeSisteAar = true;
        engangsstonad.jobbetINorgeSisteTolvMnd = true;
        engangsstonad.skalBoINorgeNesteTolvMnd = true;
        engangsstonad.skalFodeINorge = true;

        return engangsstonad;
    }
}
