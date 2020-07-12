package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.AnnenInntekt;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.FrilansInformasjon;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.SelvstendigNæringsdrivendeInformasjon;

public class Søker {

    private String rolle;
    private String språkkode;

    private Boolean erAleneOmOmsorg;

    private FrilansInformasjon frilansInformasjon;

    private List<SelvstendigNæringsdrivendeInformasjon> selvstendigNæringsdrivendeInformasjon = new ArrayList<>();
    private List<AnnenInntekt> andreInntekterSiste10Mnd = new ArrayList<>();

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

    public String getSpråkkode() {
        return språkkode;
    }

    public void setSpråkkode(String språkkode) {
        this.språkkode = språkkode;
    }

    public Boolean getErAleneOmOmsorg() {
        return erAleneOmOmsorg;
    }

    public void setErAleneOmOmsorg(Boolean erAleneOmOmsorg) {
        this.erAleneOmOmsorg = erAleneOmOmsorg;
    }

    public FrilansInformasjon getFrilansInformasjon() {
        return frilansInformasjon;
    }

    public void setFrilansInformasjon(FrilansInformasjon frilansInformasjon) {
        this.frilansInformasjon = frilansInformasjon;
    }

    public List<SelvstendigNæringsdrivendeInformasjon> getSelvstendigNæringsdrivendeInformasjon() {
        return selvstendigNæringsdrivendeInformasjon;
    }

    public void setSelvstendigNæringsdrivendeInformasjon(
            List<SelvstendigNæringsdrivendeInformasjon> selvstendigNæringsdrivendeInformasjon) {
        this.selvstendigNæringsdrivendeInformasjon = selvstendigNæringsdrivendeInformasjon;
    }

    public List<AnnenInntekt> getAndreInntekterSiste10Mnd() {
        return andreInntekterSiste10Mnd;
    }

    public void setAndreInntekterSiste10Mnd(List<AnnenInntekt> andreInntekterSiste10Mnd) {
        this.andreInntekterSiste10Mnd = andreInntekterSiste10Mnd;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [rolle=" + getRolle() + ", språkkode=" + getSpråkkode()
                + ", erAleneOmOmsorg=" + getErAleneOmOmsorg()
                + ", frilansInformasjon="
                + getFrilansInformasjon() + ", selvstendigNæringsdrivendeInformasjon="
                + getSelvstendigNæringsdrivendeInformasjon() + ", andreInntekterSiste10Mnd="
                + getAndreInntekterSiste10Mnd()
                + "]";
    }
}
