package no.nav.foreldrepenger.selvbetjening.innsending.json.arbeid;

import no.nav.foreldrepenger.selvbetjening.innsending.json.Tidsperiode;

import java.util.List;

public class SelvstendigNæringsdrivendeInformasjon {

    public List<String> næringstyper;
    public Tidsperiode tidsperiode;
    public String navnPåNæringen;
    public String organisasjonsnummer;
    public Boolean registrertINorge;
    public String registrertILand;
    public Double stillingsprosent;
    public Boolean nyIArbeidslivet;
    public TilknyttetPerson regnskapsfører;
    public TilknyttetPerson revisor;

}
