package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.arbeid.AnnenInntekt;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.arbeid.FrilansInformasjon;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.arbeid.SelvstendigNæringsdrivendeInformasjon;

public class Søker {

    public String rolle;
    public String språkKode;

    public Boolean erAleneOmOmsorg;

    public FrilansInformasjon frilansInformasjon;

    public List<SelvstendigNæringsdrivendeInformasjon> selvstendigNæringsdrivendeInformasjon = new ArrayList<>();
    public List<AnnenInntekt> andreInntekterSiste10Mnd = new ArrayList<>();

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [rolle=" + rolle + ", språkKode=" + språkKode
                + ", erAleneOmOmsorg=" + erAleneOmOmsorg
                + ", frilansInformasjon="
                + frilansInformasjon + ", selvstendigNæringsdrivendeInformasjon="
                + selvstendigNæringsdrivendeInformasjon + ", andreInntekterSiste10Mnd=" + andreInntekterSiste10Mnd
                + "]";
    }

}
