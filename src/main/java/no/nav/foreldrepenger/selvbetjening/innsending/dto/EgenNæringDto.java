package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.NæringsinntektInformasjon;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.SelvstendigNæringsdrivendeInformasjon;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.TilknyttetPerson;
import no.nav.foreldrepenger.selvbetjening.util.DateUtil;

@JsonInclude(NON_EMPTY)
public class EgenNæringDto {
    @Override
    public String toString() {
        return "EgenNæringDto [type=" + type + ", periode=" + periode + ", orgName=" + orgName + ", orgNummer="
                + orgNummer + ", virksomhetsTyper=" + virksomhetsTyper + ", registrertILand=" + registrertILand
                + ", regnskapsførere=" + regnskapsførere + ", næringsinntektBrutto=" + næringsinntektBrutto
                + ", erNyOpprettet=" + erNyOpprettet + ", erNyIArbeidslivet=" + erNyIArbeidslivet + ", nærRelasjon="
                + nærRelasjon + ", erVarigEndring=" + erVarigEndring + ", oppstartsDato=" + oppstartsDato
                + ", endringsDato=" + endringsDato + ", beskrivelseEndring=" + beskrivelseEndring + ", vedlegg="
                + vedlegg + "]";
    }

    public String type;
    public PeriodeDto periode;
    public String orgName;
    public String orgNummer;
    public Double stillingsprosent;
    public List<String> virksomhetsTyper = new ArrayList<>();
    public String registrertILand;
    public List<RegnskapsførerDto> regnskapsførere = new ArrayList<>();
    public Integer næringsinntektBrutto;
    public Boolean erNyOpprettet;
    public Boolean erNyIArbeidslivet;
    public Boolean nærRelasjon;
    public Boolean erVarigEndring;
    public LocalDate oppstartsDato;
    public LocalDate endringsDato;
    public String beskrivelseEndring;

    public List<String> vedlegg;

    public EgenNæringDto(SelvstendigNæringsdrivendeInformasjon selvstendig) {
        NæringsinntektInformasjon næringsInfo = selvstendig.getEndringAvNæringsinntektInformasjon();
        TilknyttetPerson regnskapsfører = selvstendig.getRegnskapsfører();
        TilknyttetPerson revisor = selvstendig.getRevisor();

        this.type = selvstendig.getRegistrertINorge() ? "norsk" : "utenlandsk";
        this.stillingsprosent = selvstendig.getStillingsprosent();
        this.orgNummer = selvstendig.getRegistrertINorge() ? selvstendig.getOrganisasjonsnummer() : null;
        this.orgName = selvstendig.getNavnPåNæringen();
        this.periode = new PeriodeDto(selvstendig.getTidsperiode().getFom(), selvstendig.getTidsperiode().getTom());
        this.registrertILand = selvstendig.getRegistrertILand();
        this.erNyIArbeidslivet = selvstendig.getHarBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene();
        this.erNyOpprettet = DateUtil.erNyopprettet(periode.fom());
        this.erVarigEndring = selvstendig.getHattVarigEndringAvNæringsinntektSiste4Kalenderår();
        this.vedlegg = selvstendig.getVedlegg();
        this.virksomhetsTyper.addAll(selvstendig.getNæringstyper());
        this.oppstartsDato = selvstendig.getOppstartsdato();

        if (næringsInfo != null) {
            this.endringsDato = næringsInfo.getDato();
            this.næringsinntektBrutto = næringsInfo.getNæringsinntektEtterEndring();
            this.beskrivelseEndring = næringsInfo.getForklaring();
        } else {
            this.næringsinntektBrutto = selvstendig.getNæringsinntekt();
        }

        if (regnskapsfører != null) {
            regnskapsførere.add(RegnskapsførerDto.from(regnskapsfører));
            this.nærRelasjon = regnskapsfører.getErNærVennEllerFamilie();
        } else if (revisor != null) {
            regnskapsførere.add(RegnskapsførerDto.from(revisor));
            this.nærRelasjon = revisor.getErNærVennEllerFamilie();
        }
    }
}