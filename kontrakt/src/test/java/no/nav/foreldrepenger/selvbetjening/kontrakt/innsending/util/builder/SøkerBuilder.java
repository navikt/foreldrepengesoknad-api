package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder;

import java.util.List;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.AnnenInntektDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.FrilansInformasjonDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.NæringDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDtoOLD;

public final class SøkerBuilder {
    private BrukerRolle rolle;
    private Målform språkkode;
    private boolean erAleneOmOmsorg;
    private FrilansInformasjonDtoOLD frilansInformasjon;
    private List<NæringDtoOLD> selvstendigNæringsdrivendeInformasjon;
    private List<AnnenInntektDtoOLD> andreInntekterSiste10Mnd;

    public SøkerBuilder(BrukerRolle rolle) {
        this.rolle = rolle;
        this.språkkode = Målform.standard();
    }

    public SøkerBuilder medRolle(BrukerRolle rolle) {
        this.rolle = rolle;
        return this;
    }

    public SøkerBuilder medSpråkkode(Målform språkkode) {
        this.språkkode = språkkode;
        return this;
    }

    public SøkerBuilder medErAleneOmOmsorg(boolean erAleneOmOmsorg) {
        this.erAleneOmOmsorg = erAleneOmOmsorg;
        return this;
    }

    public SøkerBuilder medFrilansInformasjon(FrilansInformasjonDtoOLD frilansInformasjon) {
        this.frilansInformasjon = frilansInformasjon;
        return this;
    }

    public SøkerBuilder medSelvstendigNæringsdrivendeInformasjon(List<NæringDtoOLD> selvstendigNæringsdrivendeInformasjon) {
        this.selvstendigNæringsdrivendeInformasjon = selvstendigNæringsdrivendeInformasjon;
        return this;
    }

    public SøkerBuilder medAndreInntekterSiste10Mnd(List<AnnenInntektDtoOLD> andreInntekterSiste10Mnd) {
        this.andreInntekterSiste10Mnd = andreInntekterSiste10Mnd;
        return this;
    }

    public SøkerDtoOLD build() {
        return new SøkerDtoOLD(rolle, språkkode, erAleneOmOmsorg, frilansInformasjon, selvstendigNæringsdrivendeInformasjon,
            andreInntekterSiste10Mnd);
    }
}
