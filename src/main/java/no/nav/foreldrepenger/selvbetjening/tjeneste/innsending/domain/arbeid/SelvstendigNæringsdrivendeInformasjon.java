package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.arbeid;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Tidsperiode;

public class SelvstendigNæringsdrivendeInformasjon {

    private List<String> næringstyper;
    private Tidsperiode tidsperiode;
    private String navnPåNæringen;
    private String organisasjonsnummer;
    private Boolean registrertINorge;
    private String registrertILand;
    private Double stillingsprosent;
    private Boolean harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene;
    private LocalDate oppstartsdato;
    private TilknyttetPerson regnskapsfører;
    private TilknyttetPerson revisor;
    private Boolean hattVarigEndringAvNæringsinntektSiste4Kalenderår;
    private NæringsinntektInformasjon endringAvNæringsinntektInformasjon;
    private List<String> vedlegg;
    private Integer næringsinntekt;

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [næringstyper=" + getNæringstyper() + ", tidsperiode=" + getTidsperiode()
                + ", navnPåNæringen=" + getNavnPåNæringen() + ", organisasjonsnummer=" + getOrganisasjonsnummer()
                + ", registrertINorge=" + getRegistrertINorge() + ", registrertILand=" + getRegistrertILand()
                + ", stillingsprosent=" + getStillingsprosent()
                + ", harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene="
                + getHarBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene() + ", oppstartsdato=" + getOppstartsdato()
                + ", regnskapsfører=" + getRegnskapsfører() + ", revisor=" + getRevisor()
                + ", hattVarigEndringAvNæringsinntektSiste4Kalenderår="
                + getHattVarigEndringAvNæringsinntektSiste4Kalenderår() + ", endringAvNæringsinntektInformasjon="
                + getEndringAvNæringsinntektInformasjon() + ", vedlegg=" + getVedlegg() + ", næringsinntekt="
                + getNæringsinntekt()
                + "]";
    }

    public List<String> getNæringstyper() {
        return næringstyper;
    }

    public void setNæringstyper(List<String> næringstyper) {
        this.næringstyper = næringstyper;
    }

    public Tidsperiode getTidsperiode() {
        return tidsperiode;
    }

    public void setTidsperiode(Tidsperiode tidsperiode) {
        this.tidsperiode = tidsperiode;
    }

    public String getNavnPåNæringen() {
        return navnPåNæringen;
    }

    public void setNavnPåNæringen(String navnPåNæringen) {
        this.navnPåNæringen = navnPåNæringen;
    }

    public String getOrganisasjonsnummer() {
        return organisasjonsnummer;
    }

    public void setOrganisasjonsnummer(String organisasjonsnummer) {
        this.organisasjonsnummer = organisasjonsnummer;
    }

    public Boolean getRegistrertINorge() {
        return registrertINorge;
    }

    public void setRegistrertINorge(Boolean registrertINorge) {
        this.registrertINorge = registrertINorge;
    }

    public String getRegistrertILand() {
        return registrertILand;
    }

    public void setRegistrertILand(String registrertILand) {
        this.registrertILand = registrertILand;
    }

    public Double getStillingsprosent() {
        return stillingsprosent;
    }

    public void setStillingsprosent(Double stillingsprosent) {
        this.stillingsprosent = stillingsprosent;
    }

    public Boolean getHarBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene() {
        return harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene;
    }

    public void setHarBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene(
            Boolean harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene) {
        this.harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene = harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene;
    }

    public LocalDate getOppstartsdato() {
        return oppstartsdato;
    }

    public void setOppstartsdato(LocalDate oppstartsdato) {
        this.oppstartsdato = oppstartsdato;
    }

    public TilknyttetPerson getRegnskapsfører() {
        return regnskapsfører;
    }

    public void setRegnskapsfører(TilknyttetPerson regnskapsfører) {
        this.regnskapsfører = regnskapsfører;
    }

    public TilknyttetPerson getRevisor() {
        return revisor;
    }

    public void setRevisor(TilknyttetPerson revisor) {
        this.revisor = revisor;
    }

    public Boolean getHattVarigEndringAvNæringsinntektSiste4Kalenderår() {
        return hattVarigEndringAvNæringsinntektSiste4Kalenderår;
    }

    public void setHattVarigEndringAvNæringsinntektSiste4Kalenderår(
            Boolean hattVarigEndringAvNæringsinntektSiste4Kalenderår) {
        this.hattVarigEndringAvNæringsinntektSiste4Kalenderår = hattVarigEndringAvNæringsinntektSiste4Kalenderår;
    }

    public NæringsinntektInformasjon getEndringAvNæringsinntektInformasjon() {
        return endringAvNæringsinntektInformasjon;
    }

    public void setEndringAvNæringsinntektInformasjon(NæringsinntektInformasjon endringAvNæringsinntektInformasjon) {
        this.endringAvNæringsinntektInformasjon = endringAvNæringsinntektInformasjon;
    }

    public List<String> getVedlegg() {
        return vedlegg;
    }

    public void setVedlegg(List<String> vedlegg) {
        this.vedlegg = vedlegg;
    }

    public Integer getNæringsinntekt() {
        return næringsinntekt;
    }

    public void setNæringsinntekt(Integer næringsinntekt) {
        this.næringsinntekt = næringsinntekt;
    }

}
