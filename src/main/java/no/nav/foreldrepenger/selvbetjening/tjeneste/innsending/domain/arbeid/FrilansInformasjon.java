package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.arbeid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FrilansInformasjon {

    private LocalDate oppstart;
    private Boolean driverFosterhjem;
    private List<Frilansoppdrag> oppdragForNæreVennerEllerFamilieSiste10Mnd = new ArrayList<>();

    public LocalDate getOppstart() {
        return oppstart;
    }

    public void setOppstart(LocalDate oppstart) {
        this.oppstart = oppstart;
    }

    public Boolean getDriverFosterhjem() {
        return driverFosterhjem;
    }

    public void setDriverFosterhjem(Boolean driverFosterhjem) {
        this.driverFosterhjem = driverFosterhjem;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [oppstart=" + getOppstart() + ", driverFosterhjem="
                + getDriverFosterhjem()
                + ", oppdragForNæreVennerEllerFamilieSiste10Mnd=" + getOppdragForNæreVennerEllerFamilieSiste10Mnd() + "]";
    }

    public List<Frilansoppdrag> getOppdragForNæreVennerEllerFamilieSiste10Mnd() {
        return oppdragForNæreVennerEllerFamilieSiste10Mnd;
    }

    public void setOppdragForNæreVennerEllerFamilieSiste10Mnd(List<Frilansoppdrag> oppdragForNæreVennerEllerFamilieSiste10Mnd) {
        this.oppdragForNæreVennerEllerFamilieSiste10Mnd = oppdragForNæreVennerEllerFamilieSiste10Mnd;
    }
}
