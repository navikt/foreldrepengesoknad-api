package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Tidsperiode;

public class Frilansoppdrag {

    private String navnPåArbeidsgiver;
    private Tidsperiode tidsperiode;

    public String getNavnPåArbeidsgiver() {
        return navnPåArbeidsgiver;
    }

    public void setNavnPåArbeidsgiver(String navnPåArbeidsgiver) {
        this.navnPåArbeidsgiver = navnPåArbeidsgiver;
    }

    public Tidsperiode getTidsperiode() {
        return tidsperiode;
    }

    public void setTidsperiode(Tidsperiode tidsperiode) {
        this.tidsperiode = tidsperiode;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [navnPåArbeidsgiver=" + getNavnPåArbeidsgiver() + ", tidsperiode="
                + getTidsperiode() + "]";
    }
}
