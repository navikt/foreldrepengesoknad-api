package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import no.nav.foreldrepenger.selvbetjening.innsending.json.arbeid.*;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Søker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
            if (annenInntekt.type.equals("jobbiutlandet")) {
                this.arbeidsforhold.add(new ArbeidsforholdDto(annenInntekt));
            } else {
                this.annenOpptjening.add(new AnnenOpptjeningDto(annenInntekt));
            }
        }
    }

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

    public class EgenNæringDto {
        public String type;
        public PeriodeDto periode = new PeriodeDto();
        public String orgName;
        public String orgNummer;
        public List<String> virksomhetsTyper = new ArrayList<>();
        public String arbeidsland;
        public List<RegnskapsførerDto> regnskapsførere = new ArrayList<>();

        public EgenNæringDto(SelvstendigNæringsdrivendeInformasjon selvstendig) {
            this.type = selvstendig.registrertINorge ? "norsk" : "utenlandsk";
            this.orgNummer = selvstendig.registrertINorge ? selvstendig.organisasjonsnummer : null;
            this.orgName = selvstendig.navnPåNæringen;
            this.periode.fom = selvstendig.tidsperiode.fom;
            this.periode.tom = selvstendig.tidsperiode.tom;
            this.arbeidsland = selvstendig.registrertILand;

            this.virksomhetsTyper.addAll(selvstendig.næringstyper);

            if (selvstendig.regnskapsfører != null) {
                regnskapsførere.add(new RegnskapsførerDto(selvstendig.regnskapsfører));
            } else if (selvstendig.revisor != null) {
                regnskapsførere.add(new RegnskapsførerDto(selvstendig.revisor));
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

        public ArbeidsforholdDto(AnnenInntekt annenInntekt) {
            this.arbeidsgiverNavn = annenInntekt.arbeidsgiverNavn; 
            this.land = annenInntekt.land;
            this.periode.fom = annenInntekt.tidsperiode.fom;
            this.periode.tom = annenInntekt.tidsperiode.tom;
        }
    }
}
