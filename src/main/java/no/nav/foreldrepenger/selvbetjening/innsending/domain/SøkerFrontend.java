package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.AnnenInntektFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.FrilansInformasjonFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.SelvstendigNæringsdrivendeInformasjonFrontend;

public record SøkerFrontend(BrukerRolle rolle,
                            Målform språkkode,
                            Boolean erAleneOmOmsorg,
                            @Valid FrilansInformasjonFrontend frilansInformasjon,
                            @Valid List<SelvstendigNæringsdrivendeInformasjonFrontend> selvstendigNæringsdrivendeInformasjon,
                            @Valid List<AnnenInntektFrontend> andreInntekterSiste10Mnd) {

    public SøkerFrontend(BrukerRolle rolle, Målform språkkode, Boolean erAleneOmOmsorg, FrilansInformasjonFrontend frilansInformasjon,
                         List<SelvstendigNæringsdrivendeInformasjonFrontend> selvstendigNæringsdrivendeInformasjon,
                         List<AnnenInntektFrontend> andreInntekterSiste10Mnd) {
        this.rolle = rolle;
        this.språkkode = språkkode;
        this.erAleneOmOmsorg = erAleneOmOmsorg;
        this.frilansInformasjon = frilansInformasjon;
        this.selvstendigNæringsdrivendeInformasjon = Optional.ofNullable(selvstendigNæringsdrivendeInformasjon).orElse(emptyList());
        this.andreInntekterSiste10Mnd = Optional.ofNullable(andreInntekterSiste10Mnd).orElse(emptyList());
    }
}
