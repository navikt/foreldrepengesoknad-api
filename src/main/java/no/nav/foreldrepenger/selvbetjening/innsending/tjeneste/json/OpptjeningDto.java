package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import no.nav.foreldrepenger.selvbetjening.innsending.json.arbeid.*;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Søker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

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
            } else {
                this.annenOpptjening.add(new AnnenOpptjeningDto(annenInntekt));
            }
        }
    }

    @JsonInclude(NON_EMPTY)
    public class FrilansDto {
        public PeriodeDto periode = new PeriodeDto();
        public Boolean harInntektFraFosterhjem;
        public List<FrilansoppdragDto> frilansOppdrag = new ArrayList<>();

        public FrilansDto(FrilansInformasjon frilansInformasjon) {
            this.periode.fom = frilansInformasjon.oppstart;
            this.harInntektFraFosterhjem = frilansInformasjon.driverFosterhjem;

            for (Frilansoppdrag o : frilansInformasjon.oppdragForNæreVennerEllerFamilieSiste10Mnd) {
                frilansOppdrag.add(new FrilansoppdragDto(o.navnPåArbeidsgiver, o.tidsperiode.fom, o.tidsperiode.tom));
            }
        }
    }

    public class FrilansoppdragDto {
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
        public String type;
        public PeriodeDto periode = new PeriodeDto();
        public String orgName;
        public String orgNummer;
        public List<String> virksomhetsTyper = new ArrayList<>();
        public String arbeidsland;
        public List<RegnskapsførerDto> regnskapsførere = new ArrayList<>();
        public int naeringsinntektBrutto;
        public Boolean erNyoppstartet;
        public Boolean erNyIArbeidslivet;
        public Boolean naerRelasjon;
        public Boolean erVarigEndring;
        public LocalDate oppstartsDato;
        public LocalDate endringsDato;
        public String beskrivelseAvEndring;

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

            this.virksomhetsTyper.addAll(selvstendig.næringstyper);

            if (næringsInfo != null) {
                this.endringsDato = næringsInfo.dato;
                this.naeringsinntektBrutto = Integer.parseInt(næringsInfo.næringsinntektEtterEndring);
                this.beskrivelseAvEndring = næringsInfo.forklaring;
            }

            if (regnskapsfører != null) {
                regnskapsførere.add(new RegnskapsførerDto(regnskapsfører));
                this.naerRelasjon = regnskapsfører.erNærVennEllerFamilie;
            } else if (revisor != null) {
                regnskapsførere.add(new RegnskapsførerDto(revisor));
                this.naerRelasjon = revisor.erNærVennEllerFamilie;
            }
        }
    }

    public class RegnskapsførerDto {
        public String navn;
        public String telefon;

        public RegnskapsførerDto(TilknyttetPerson person) {
            this.navn = person.navn;
            this.telefon = person.telefonnummer;
        }
    }

    public class AnnenOpptjeningDto {
        public String type;
        public PeriodeDto periode = new PeriodeDto();

        public AnnenOpptjeningDto(AnnenInntekt annenInntekt) {
            this.type = annenInntekt.type;
            this.periode.fom = annenInntekt.tidsperiode.fom;
            this.periode.tom = annenInntekt.tidsperiode.tom;
        }
    }

    public class ArbeidsforholdDto {
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
