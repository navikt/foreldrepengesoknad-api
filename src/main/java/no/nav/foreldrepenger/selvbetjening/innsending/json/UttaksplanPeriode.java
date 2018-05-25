package no.nav.foreldrepenger.selvbetjening.innsending.json;

import java.time.LocalDate;

public class UttaksplanPeriode {
    public String type;
    public String årsak;
    public String konto;
    public Tidsperiode tidsperiode;
    public String forelder;

    public class Tidsperiode {
        public LocalDate startdato;
        public LocalDate sluttdato;
    }
}
