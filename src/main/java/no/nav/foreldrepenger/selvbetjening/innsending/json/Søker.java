package no.nav.foreldrepenger.selvbetjening.innsending.json;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.innsending.json.arbeid.AnnenInntekt;
import no.nav.foreldrepenger.selvbetjening.innsending.json.arbeid.FrilansInformasjon;
import no.nav.foreldrepenger.selvbetjening.innsending.json.arbeid.SelvstendigNæringsdrivendeInformasjon;

public class Søker {

    public String rolle;

    public Boolean erAleneOmOmsorg;

    public FrilansInformasjon frilansInformasjon;
    public List<SelvstendigNæringsdrivendeInformasjon> selvstendigNæringsdrivendeInformasjon = new ArrayList<>();
    public List<AnnenInntekt> andreInntekterSiste10Mnd = new ArrayList<>();

}
