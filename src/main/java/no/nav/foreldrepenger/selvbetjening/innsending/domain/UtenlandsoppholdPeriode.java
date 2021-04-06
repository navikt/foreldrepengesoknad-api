package no.nav.foreldrepenger.selvbetjening.innsending.domain;

public class UtenlandsoppholdPeriode {

    private String land;
    private Tidsperiode tidsperiode;

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [land=" + getLand() + ", tidsperiode=" + getTidsperiode() + "]";
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public Tidsperiode getTidsperiode() {
        return tidsperiode;
    }

    public void setTidsperiode(Tidsperiode tidsperiode) {
        this.tidsperiode = tidsperiode;
    }
}
