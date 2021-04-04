package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.AnnenInntekt;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.FrilansInformasjon;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.SelvstendigNæringsdrivendeInformasjon;

public record Søker(
        String rolle,
        String språkkode,
        Boolean erAleneOmOmsorg,
        FrilansInformasjon frilansInformasjon,
        List<SelvstendigNæringsdrivendeInformasjon> selvstendigNæringsdrivendeInformasjon,
        List<AnnenInntekt> andreInntekterSiste10Mnd) {

}
