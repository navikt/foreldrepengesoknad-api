package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.time.LocalDate.now;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Søker;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.arbeid.AnnenInntekt;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.arbeid.FrilansInformasjon;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.arbeid.Frilansoppdrag;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.arbeid.NæringsinntektInformasjon;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.arbeid.SelvstendigNæringsdrivendeInformasjon;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.arbeid.TilknyttetPerson;

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

        public PeriodeDto periode = new PeriodeDto();
        public Boolean harInntektFraFosterhjem;
        public Boolean nyOppstartet;
        public List<FrilansoppdragDto> frilansOppdrag = new ArrayList<>();

        public FrilansDto(FrilansInformasjon frilansInformasjon) {
            this.periode.fom = frilansInformasjon.getOppstart();
            this.harInntektFraFosterhjem = frilansInformasjon.getDriverFosterhjem();

            LocalDate treMånederFørFom = now().minus(Period.ofDays(90));
            this.nyOppstartet = this.periode.fom.isAfter(treMånederFørFom);

            for (Frilansoppdrag o : frilansInformasjon.getOppdragForNæreVennerEllerFamilieSiste10Mnd()) {
                frilansOppdrag.add(new FrilansoppdragDto(o.getNavnPåArbeidsgiver(), o.getTidsperiode().getFom(),
                        o.getTidsperiode().getTom()));
            }
        }
    }

    public class FrilansoppdragDto {
        @Override
        public String toString() {
            return "FrilansoppdragDto [oppdragsgiver=" + oppdragsgiver + ", periode=" + periode + "]";
        }

        public String oppdragsgiver;
        public PeriodeDto periode = new PeriodeDto();

        public FrilansoppdragDto(String oppdragsgiver, LocalDate fom, LocalDate tom) {
            this.oppdragsgiver = oppdragsgiver;
            this.periode.fom = fom;
            this.periode.tom = tom;
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
        public PeriodeDto periode = new PeriodeDto();
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

            LocalDate fireÅrSiden = now().minusYears(4);

            this.type = selvstendig.getRegistrertINorge() ? "norsk" : "utenlandsk";
            this.stillingsprosent = selvstendig.getStillingsprosent();
            this.orgNummer = selvstendig.getRegistrertINorge() ? selvstendig.getOrganisasjonsnummer() : null;
            this.orgName = selvstendig.getNavnPåNæringen();
            this.periode.fom = selvstendig.getTidsperiode().getFom();
            this.periode.tom = selvstendig.getTidsperiode().getTom();
            this.registrertILand = selvstendig.getRegistrertILand();
            this.erNyIArbeidslivet = selvstendig.getHarBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene();
            this.erNyOpprettet = this.periode.fom.isAfter(fireÅrSiden.minusDays(1));
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
                regnskapsførere.add(new RegnskapsførerDto(regnskapsfører));
                this.nærRelasjon = regnskapsfører.getErNærVennEllerFamilie();
            } else if (revisor != null) {
                regnskapsførere.add(new RegnskapsførerDto(revisor));
                this.nærRelasjon = revisor.getErNærVennEllerFamilie();
            }
        }
    }

    public class RegnskapsførerDto {
        @Override
        public String toString() {
            return "RegnskapsførerDto [navn=" + navn + ", telefon=" + telefon + "]";
        }

        public String navn;
        public String telefon;

        public RegnskapsførerDto(TilknyttetPerson person) {
            this.navn = person.getNavn();
            this.telefon = person.getTelefonnummer();
        }
    }

    public class AnnenOpptjeningDto {
        @Override
        public String toString() {
            return "AnnenOpptjeningDto [type=" + type + ", periode=" + periode + "]";
        }

        public String type;
        public PeriodeDto periode = new PeriodeDto();
        public List<String> vedlegg;

        public AnnenOpptjeningDto(AnnenInntekt annenInntekt) {
            this.type = annenInntekt.getType();
            this.periode.fom = annenInntekt.getTidsperiode().getFom();
            this.periode.tom = annenInntekt.getTidsperiode().getTom();
            this.vedlegg = annenInntekt.getVedlegg();
        }
    }

    public class ArbeidsforholdDto {
        @Override
        public String toString() {
            return "ArbeidsforholdDto [arbeidsgiverNavn=" + arbeidsgiverNavn + ", periode=" + periode + ", land=" + land
                    + ", vedlegg=" + vedlegg + "]";
        }

        public String arbeidsgiverNavn;
        public PeriodeDto periode = new PeriodeDto();
        public String land;
        public List<String> vedlegg;

        public ArbeidsforholdDto(AnnenInntekt annenInntekt) {
            this.arbeidsgiverNavn = annenInntekt.getArbeidsgiverNavn();
            this.land = annenInntekt.getLand();
            this.periode.fom = annenInntekt.getTidsperiode().getFom();
            this.periode.tom = annenInntekt.getTidsperiode().getTom();
            this.vedlegg = annenInntekt.getVedlegg();
        }
    }
}
