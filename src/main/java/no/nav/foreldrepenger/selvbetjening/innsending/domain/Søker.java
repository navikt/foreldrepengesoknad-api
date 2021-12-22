package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.AnnenInntekt;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.FrilansInformasjon;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.SelvstendigNæringsdrivendeInformasjon;

public record Søker(String rolle,
                    String språkkode,
                    Boolean erAleneOmOmsorg,
                    FrilansInformasjon frilansInformasjon,
                    List<SelvstendigNæringsdrivendeInformasjon> selvstendigNæringsdrivendeInformasjon,
                    List<AnnenInntekt> andreInntekterSiste10Mnd) {

    public Søker(String rolle, String språkkode, Boolean erAleneOmOmsorg, FrilansInformasjon frilansInformasjon,
                 List<SelvstendigNæringsdrivendeInformasjon> selvstendigNæringsdrivendeInformasjon,
                 List<AnnenInntekt> andreInntekterSiste10Mnd) {
        this.rolle = rolle;
        this.språkkode = språkkode;
        this.erAleneOmOmsorg = erAleneOmOmsorg;
        this.frilansInformasjon = frilansInformasjon;
        this.selvstendigNæringsdrivendeInformasjon = Optional.ofNullable(selvstendigNæringsdrivendeInformasjon).orElse(emptyList());
        this.andreInntekterSiste10Mnd = Optional.ofNullable(andreInntekterSiste10Mnd).orElse(emptyList());
    }
}
