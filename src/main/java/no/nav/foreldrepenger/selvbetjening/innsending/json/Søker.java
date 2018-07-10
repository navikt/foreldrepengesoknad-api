package no.nav.foreldrepenger.selvbetjening.innsending.json;

import no.nav.foreldrepenger.selvbetjening.innsending.json.arbeid.FrilansInformasjon;
import no.nav.foreldrepenger.selvbetjening.innsending.json.arbeid.SelvstendigNæringsdrivendeInformasjon;

import java.util.List;

public class Søker {

    public String rolle;

    public Boolean erAleneOmOmsorg;

    public Boolean harJobbetSomFrilansSiste10Mnd;
    public Boolean harJobbetSomSelvstendigNæringsdrivendeSiste10Mnd;
    public Boolean harHattAnnenInntektSiste10Mnd;

    public FrilansInformasjon frilansInformasjon;
    public List<SelvstendigNæringsdrivendeInformasjon> selvstendigNæringsdrivendeInformasjon;

}
