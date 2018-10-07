package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.innsending.json.Søker;
import no.nav.foreldrepenger.selvbetjening.innsending.json.arbeid.AnnenInntekt;
import no.nav.foreldrepenger.selvbetjening.innsending.json.arbeid.FrilansInformasjon;
import no.nav.foreldrepenger.selvbetjening.innsending.json.arbeid.Frilansoppdrag;
import no.nav.foreldrepenger.selvbetjening.innsending.json.arbeid.NæringsinntektInformasjon;
import no.nav.foreldrepenger.selvbetjening.innsending.json.arbeid.SelvstendigNæringsdrivendeInformasjon;
import no.nav.foreldrepenger.selvbetjening.innsending.json.arbeid.TilknyttetPerson;

@JsonInclude(NON_EMPTY)
public class OpptjeningDto {

    public FrilansDto frilans;
    public List<EgenNæringDto> egenNæring = new ArrayList<>();
    public List<AnnenOpptjeningDto> annenOpptjening = new ArrayList<>();
    public List<ArbeidsforholdDto> arbeidsforhold = new ArrayList<>();

    public OpptjeningDto(Søker søker) {
        if (søker.frilansInformasjon != null) {
            this.frilans = new FrilansDto(søker.frilansInformasjon);
        }
        for (SelvstendigNæringsdrivendeInformasjon selvstendig : søker.selvstendigNæringsdrivendeInformasjon) {
            this.egenNæring.add(new EgenNæringDto(selvstendig));
        }
        for (AnnenInntekt annenInntekt : søker.andreInntekterSiste10Mnd) {
            if (annenInntekt.type.equals("JOBB_I_UTLANDET")) {
                this.arbeidsforhold.add(new ArbeidsforholdDto(annenInntekt));
            }
            else {
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
            this.periode.fom = frilansInformasjon.oppstart;
            this.harInntektFraFosterhjem = frilansInformasjon.driverFosterhjem;

            LocalDate treMånederFørFom = LocalDate.now().minus(Period.ofDays(90));
            this.nyOppstartet = this.periode.fom.isAfter(treMånederFørFom);

            for (Frilansoppdrag o : frilansInformasjon.oppdragForNæreVennerEllerFamilieSiste10Mnd) {
                frilansOppdrag.add(new FrilansoppdragDto(o.navnPåArbeidsgiver, o.tidsperiode.fom, o.tidsperiode.tom));
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
                    + orgNummer + ", virksomhetsTyper=" + virksomhetsTyper + ", arbeidsland=" + arbeidsland
                    + ", regnskapsførere=" + regnskapsførere + ", næringsinntektBrutto=" + næringsinntektBrutto
                    + ", erNyOpprettet=" + erNyOpprettet + ", erNyIArbeidslivet=" + erNyIArbeidslivet + ", nærRelasjon="
                    + nærRelasjon + ", erVarigEndring=" + erVarigEndring + ", oppstartsDato=" + oppstartsDato
                    + ", endringsDato=" + endringsDato + ", beskrivelseEndring=" + beskrivelseEndring + ", vedlegg=" + vedlegg +"]";
        }

        public String type;
        public PeriodeDto periode = new PeriodeDto();
        public String orgName;
        public String orgNummer;
        public List<String> virksomhetsTyper = new ArrayList<>();
        public String arbeidsland;
        public List<RegnskapsførerDto> regnskapsførere = new ArrayList<>();
        public int næringsinntektBrutto;
        public Boolean erNyOpprettet;
        public Boolean erNyIArbeidslivet;
        public Boolean nærRelasjon;
        public Boolean erVarigEndring;
        public LocalDate oppstartsDato;
        public LocalDate endringsDato;
        public String beskrivelseEndring;
        public List<String> vedlegg;

        public EgenNæringDto(SelvstendigNæringsdrivendeInformasjon selvstendig) {
            NæringsinntektInformasjon næringsInfo = selvstendig.endringAvNæringsinntektInformasjon;
            TilknyttetPerson regnskapsfører = selvstendig.regnskapsfører;
            TilknyttetPerson revisor = selvstendig.revisor;

            this.type = selvstendig.registrertINorge ? "norsk" : "utenlandsk";
            this.orgNummer = selvstendig.registrertINorge ? selvstendig.organisasjonsnummer : null;
            this.orgName = selvstendig.navnPåNæringen;
            this.periode.fom = selvstendig.tidsperiode.fom;
            this.periode.tom = selvstendig.tidsperiode.tom;
            this.arbeidsland = selvstendig.registrertILand;
            this.erNyIArbeidslivet = selvstendig.nyIArbeidslivet;
            this.erVarigEndring = selvstendig.hattVarigEndringAvNæringsinntektSiste4Kalenderår;
            this.vedlegg = selvstendig.vedlegg;

            this.virksomhetsTyper.addAll(selvstendig.næringstyper);

            if (næringsInfo != null) {
                this.endringsDato = næringsInfo.dato;
                this.næringsinntektBrutto = Integer.parseInt(næringsInfo.næringsinntektEtterEndring);
                this.beskrivelseEndring = næringsInfo.forklaring;
            }

            if (regnskapsfører != null) {
                regnskapsførere.add(new RegnskapsførerDto(regnskapsfører));
                this.nærRelasjon = regnskapsfører.erNærVennEllerFamilie;
            }
            else if (revisor != null) {
                regnskapsførere.add(new RegnskapsførerDto(revisor));
                this.nærRelasjon = revisor.erNærVennEllerFamilie;
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
            this.navn = person.navn;
            this.telefon = person.telefonnummer;
        }
    }

    public class AnnenOpptjeningDto {
        @Override
        public String toString() {
            return "AnnenOpptjeningDto [type=" + type + ", periode=" + periode + "]";
        }

        public String type;
        public PeriodeDto periode = new PeriodeDto();

        public AnnenOpptjeningDto(AnnenInntekt annenInntekt) {
            this.type = annenInntekt.type;
            this.periode.fom = annenInntekt.tidsperiode.fom;
            this.periode.tom = annenInntekt.tidsperiode.tom;
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
            this.arbeidsgiverNavn = annenInntekt.arbeidsgiverNavn;
            this.land = annenInntekt.land;
            this.periode.fom = annenInntekt.tidsperiode.fom;
            this.periode.tom = annenInntekt.tidsperiode.tom;
            this.vedlegg = annenInntekt.vedlegg;
        }
    }
}
