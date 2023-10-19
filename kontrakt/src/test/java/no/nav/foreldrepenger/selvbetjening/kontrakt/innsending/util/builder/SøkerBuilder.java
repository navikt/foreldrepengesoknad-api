package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder;

import java.util.List;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.AnnenInntektDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.FrilansInformasjonDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.NæringDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDto;

public final class SøkerBuilder {
    private BrukerRolle rolle;
    private Målform språkkode;
    private boolean erAleneOmOmsorg;
    private FrilansInformasjonDto frilansInformasjon;
    private List<NæringDto> selvstendigNæringsdrivendeInformasjon;
    private List<AnnenInntektDto> andreInntekterSiste10Mnd;

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

    public SøkerBuilder medFrilansInformasjon(FrilansInformasjonDto frilansInformasjon) {
        this.frilansInformasjon = frilansInformasjon;
        return this;
    }

    public SøkerBuilder medSelvstendigNæringsdrivendeInformasjon(List<NæringDto> selvstendigNæringsdrivendeInformasjon) {
        this.selvstendigNæringsdrivendeInformasjon = selvstendigNæringsdrivendeInformasjon;
        return this;
    }

    public SøkerBuilder medAndreInntekterSiste10Mnd(List<AnnenInntektDto> andreInntekterSiste10Mnd) {
        this.andreInntekterSiste10Mnd = andreInntekterSiste10Mnd;
        return this;
    }

    public SøkerDto build() {
        return new SøkerDto(
                rolle,
                språkkode,
                erAleneOmOmsorg,
                frilansInformasjon,
                selvstendigNæringsdrivendeInformasjon,
                andreInntekterSiste10Mnd
        );
    }
}
