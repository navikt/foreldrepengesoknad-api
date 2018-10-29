package no.nav.foreldrepenger.selvbetjening.innsending.json.arbeid;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.innsending.json.Tidsperiode;

public class SelvstendigNæringsdrivendeInformasjon {

    public List<String> næringstyper;
    public Tidsperiode tidsperiode;
    public String navnPåNæringen;
    public String organisasjonsnummer;
    public Boolean registrertINorge;
    public String registrertILand;
    public Double stillingsprosent;
    public Boolean harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene;
    public LocalDate oppstartsdato;
    public TilknyttetPerson regnskapsfører;
    public TilknyttetPerson revisor;
    public Boolean hattVarigEndringAvNæringsinntektSiste4Kalenderår;
    public NæringsinntektInformasjon endringAvNæringsinntektInformasjon;
    public List<String> vedlegg;


}
