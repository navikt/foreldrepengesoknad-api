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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((andreInntekterSiste10Mnd == null) ? 0 : andreInntekterSiste10Mnd.hashCode());
        result = prime * result + ((erAleneOmOmsorg == null) ? 0 : erAleneOmOmsorg.hashCode());
        result = prime * result + ((frilansInformasjon == null) ? 0 : frilansInformasjon.hashCode());
        result = prime * result + ((rolle == null) ? 0 : rolle.hashCode());
        result = prime * result + ((selvstendigNæringsdrivendeInformasjon == null) ? 0 : selvstendigNæringsdrivendeInformasjon.hashCode());
        result = prime * result + ((språkkode == null) ? 0 : språkkode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Søker other = (Søker) obj;
        if (andreInntekterSiste10Mnd == null) {
            if (other.andreInntekterSiste10Mnd != null)
                return false;
        } else if (!andreInntekterSiste10Mnd.equals(other.andreInntekterSiste10Mnd))
            return false;
        if (erAleneOmOmsorg == null) {
            if (other.erAleneOmOmsorg != null)
                return false;
        } else if (!erAleneOmOmsorg.equals(other.erAleneOmOmsorg))
            return false;
        if (frilansInformasjon == null) {
            if (other.frilansInformasjon != null)
                return false;
        } else if (!frilansInformasjon.equals(other.frilansInformasjon))
            return false;
        if (rolle == null) {
            if (other.rolle != null)
                return false;
        } else if (!rolle.equals(other.rolle))
            return false;
        if (selvstendigNæringsdrivendeInformasjon == null) {
            if (other.selvstendigNæringsdrivendeInformasjon != null)
                return false;
        } else if (!selvstendigNæringsdrivendeInformasjon.equals(other.selvstendigNæringsdrivendeInformasjon))
            return false;
        if (språkkode == null) {
            if (other.språkkode != null)
                return false;
        } else if (!språkkode.equals(other.språkkode))
            return false;
        return true;
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
