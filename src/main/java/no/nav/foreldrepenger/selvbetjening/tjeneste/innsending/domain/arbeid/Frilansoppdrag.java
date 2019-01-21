package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.arbeid;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Tidsperiode;

public class Frilansoppdrag {

    public String navnPåArbeidsgiver;
    public Tidsperiode tidsperiode;

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [navnPåArbeidsgiver=" + navnPåArbeidsgiver + ", tidsperiode="
                + tidsperiode + "]";
    }
}
