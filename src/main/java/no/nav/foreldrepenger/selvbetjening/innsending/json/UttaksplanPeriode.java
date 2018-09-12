package no.nav.foreldrepenger.selvbetjening.innsending.json;

public class UttaksplanPeriode {
    public String type;
    public String årsak;
    public String konto;

    public Tidsperiode tidsperiode;
    public String forelder;

    @Override
    public String toString() {
        return "UttaksplanPeriode [type=" + type + ", årsak=" + årsak + ", konto=" + konto + ", tidsperiode="
                + tidsperiode + ", forelder=" + forelder + "]";
    }
}
