package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain;

public class UtenlandsoppholdPeriode {

    public String land;
    public Tidsperiode tidsperiode;

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [land=" + land + ", tidsperiode=" + tidsperiode + "]";
    }
}
