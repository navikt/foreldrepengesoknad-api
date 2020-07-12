package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import java.time.LocalDate;

public class NæringsinntektInformasjon {

    private LocalDate dato;
    private Integer næringsinntektEtterEndring;
    private String forklaring;

    public LocalDate getDato() {
        return dato;
    }

    public void setDato(LocalDate dato) {
        this.dato = dato;
    }

    public Integer getNæringsinntektEtterEndring() {
        return næringsinntektEtterEndring;
    }

    public void setNæringsinntektEtterEndring(Integer næringsinntektEtterEndring) {
        this.næringsinntektEtterEndring = næringsinntektEtterEndring;
    }

    public String getForklaring() {
        return forklaring;
    }

    public void setForklaring(String forklaring) {
        this.forklaring = forklaring;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [dato=" + getDato() + ", næringsinntektEtterEndring="
                + getNæringsinntektEtterEndring() + ", forklaring=" + getForklaring() + "]";
    }
}
