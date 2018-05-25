package no.nav.foreldrepenger.selvbetjening.innsending.json;

import java.time.LocalDate;

public class UtenlandsoppholdPeriode {
    public String land;
    public Varighet varighet;

    public class Varighet {
        public LocalDate fom;
        public LocalDate tom;
    }
}
