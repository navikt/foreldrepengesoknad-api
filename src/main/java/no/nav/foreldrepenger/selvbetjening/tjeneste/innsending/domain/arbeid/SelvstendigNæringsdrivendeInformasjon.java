package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.arbeid;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Tidsperiode;

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
    public Integer næringsinntekt;

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [næringstyper=" + næringstyper + ", tidsperiode=" + tidsperiode
                + ", navnPåNæringen=" + navnPåNæringen + ", organisasjonsnummer=" + organisasjonsnummer
                + ", registrertINorge=" + registrertINorge + ", registrertILand=" + registrertILand
                + ", stillingsprosent=" + stillingsprosent + ", harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene="
                + harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene + ", oppstartsdato=" + oppstartsdato
                + ", regnskapsfører=" + regnskapsfører + ", revisor=" + revisor
                + ", hattVarigEndringAvNæringsinntektSiste4Kalenderår="
                + hattVarigEndringAvNæringsinntektSiste4Kalenderår + ", endringAvNæringsinntektInformasjon="
                + endringAvNæringsinntektInformasjon + ", vedlegg=" + vedlegg + ", næringsinntekt=" + næringsinntekt
                + "]";
    }

}
