package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.time.LocalDate.now;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Søker;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.AnnenInntekt;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.FrilansInformasjon;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.Frilansoppdrag;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.NæringsinntektInformasjon;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.SelvstendigNæringsdrivendeInformasjon;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.TilknyttetPerson;
import no.nav.foreldrepenger.selvbetjening.util.DateUtil;

@JsonInclude(NON_EMPTY)
public class OpptjeningDto {

    public FrilansDto frilans;
    public List<EgenNæringDto> egenNæring = new ArrayList<>();
    public List<AnnenOpptjeningDto> annenOpptjening = new ArrayList<>();
    public List<ArbeidsforholdDto> arbeidsforhold = new ArrayList<>();

    public OpptjeningDto(Søker søker) {
        if (søker.getFrilansInformasjon() != null) {
            this.frilans = new FrilansDto(søker.getFrilansInformasjon());
        }
        for (SelvstendigNæringsdrivendeInformasjon selvstendig : søker.getSelvstendigNæringsdrivendeInformasjon()) {
            this.egenNæring.add(new EgenNæringDto(selvstendig));
        }
        for (AnnenInntekt annenInntekt : søker.getAndreInntekterSiste10Mnd()) {
            if (annenInntekt.getType().equals("JOBB_I_UTLANDET")) {
                this.arbeidsforhold.add(new ArbeidsforholdDto(annenInntekt));
            } else {
                this.annenOpptjening.add(new AnnenOpptjeningDto(annenInntekt));
            }
        }
    }

    @Override
    public String toString() {
        return "OpptjeningDto [frilans=" + frilans + ", egenNæring=" + egenNæring + ", annenOpptjening="
                + annenOpptjening + ", arbeidsforhold=" + arbeidsforhold + "]";
    }

    @JsonInclude(NON_EMPTY)
    public class FrilansDto {
        @Override
        public String toString() {
            return "FrilansDto [periode=" + periode + ", harInntektFraFosterhjem=" + harInntektFraFosterhjem
                    + ", frilansOppdrag=" + frilansOppdrag + "]";
        }

        public PeriodeDto periode;
        public Boolean harInntektFraFosterhjem;
        public Boolean nyOppstartet;
        public List<FrilansoppdragDto> frilansOppdrag = new ArrayList<>();

        public FrilansDto(FrilansInformasjon frilansInformasjon) {
            this.periode = new PeriodeDto(frilansInformasjon.getOppstart());
            this.harInntektFraFosterhjem = frilansInformasjon.getDriverFosterhjem();

            LocalDate treMånederFørFom = now().minus(Period.ofDays(90));
            this.nyOppstartet = this.periode.fom().isAfter(treMånederFørFom);

            for (Frilansoppdrag o : frilansInformasjon.getOppdragForNæreVennerEllerFamilieSiste10Mnd()) {
                frilansOppdrag.add(new FrilansoppdragDto(o.navnPåArbeidsgiver(), o.tidsperiode().getFom(),
                        o.tidsperiode().getTom()));
            }
        }
    }



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
                this.endringsDato = næringsInfo.dato();
                this.næringsinntektBrutto = næringsInfo.næringsinntektEtterEndring();
                this.beskrivelseEndring = næringsInfo.forklaring();
            } else {
                this.næringsinntektBrutto = selvstendig.getNæringsinntekt();
            }

            if (regnskapsfører != null) {
                regnskapsførere.add(new RegnskapsførerDto(regnskapsfører));
                this.nærRelasjon = regnskapsfører.erNærVennEllerFamilie();
            } else if (revisor != null) {
                regnskapsførere.add(new RegnskapsførerDto(revisor));
                this.nærRelasjon = revisor.erNærVennEllerFamilie();
            }
        }
    }

    private record RegnskapsførerDto(String navn, String telefon) {
        RegnskapsførerDto(TilknyttetPerson person) {
            this(person.navn(), person.telefonnummer());
        }
    }

    private record AnnenOpptjeningDto(String type, PeriodeDto periode, List<String> vedlegg) {

        public AnnenOpptjeningDto(AnnenInntekt annenInntekt) {
            this(annenInntekt.getType(), new PeriodeDto(annenInntekt.getTidsperiode().getFom(), annenInntekt.getTidsperiode().getTom()),
                    annenInntekt.getVedlegg());
        }
    }

    private record ArbeidsforholdDto(String arbeidsgiverNavn, PeriodeDto periode, String land, List<String> vedlegg) {
        ArbeidsforholdDto(AnnenInntekt annenInntekt) {
            this(annenInntekt.getArbeidsgiverNavn(), new PeriodeDto(annenInntekt.getTidsperiode().getFom(), annenInntekt.getTidsperiode().getTom()),
                    annenInntekt.getLand(), annenInntekt.getVedlegg());
        }
    }

    private record FrilansoppdragDto(String oppdragsgiver, PeriodeDto periode) {

        FrilansoppdragDto(String oppdragsgiver, LocalDate fom, LocalDate tom) {
            this(oppdragsgiver, new PeriodeDto(fom, tom));
        }
    }
}
