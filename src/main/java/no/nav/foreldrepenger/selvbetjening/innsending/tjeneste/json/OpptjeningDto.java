package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import no.nav.foreldrepenger.selvbetjening.innsending.json.arbeid.AnnenInntekt;
import no.nav.foreldrepenger.selvbetjening.innsending.json.arbeid.FrilansInformasjon;
import no.nav.foreldrepenger.selvbetjening.innsending.json.arbeid.Frilansoppdrag;
import no.nav.foreldrepenger.selvbetjening.innsending.json.arbeid.SelvstendigNæringsdrivendeInformasjon;
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

    public OpptjeningDto(Søker søker) {
        if (søker.frilansInformasjon != null) {
            this.frilans = new FrilansDto(søker.frilansInformasjon);
        }
        for (SelvstendigNæringsdrivendeInformasjon selvstendig : søker.selvstendigNæringsdrivendeInformasjon) {
            this.egenNæring.add(new EgenNæringDto(selvstendig));
        }
        for (AnnenInntekt annenInntekt : søker.andreInntekterSiste10Mnd) {
            this.annenOpptjening.add(new AnnenOpptjeningDto(annenInntekt));
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
                frilansOppdrag.add(new FrilansoppdragDto(o.navnPåArbeidsgiver, o.tidsperiode.startdato, o.tidsperiode.sluttdato));
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

        public EgenNæringDto(SelvstendigNæringsdrivendeInformasjon selvstendig) {
            this.type = selvstendig.registrertINorge ? "norsk" : "utenlandsk";
            this.orgNummer = selvstendig.registrertINorge ? selvstendig.organisasjonsnummer : null;
            this.orgName = selvstendig.navnPåNæringen;
            this.periode.fom = selvstendig.tidsperiode.startdato;
            this.periode.tom = selvstendig.tidsperiode.sluttdato;
            this.arbeidsland = selvstendig.registrertILand;

            this.virksomhetsTyper.addAll(selvstendig.næringstyper);
        }
    }

    public class AnnenOpptjeningDto {
        public String type;
        public PeriodeDto periode = new PeriodeDto();

        public AnnenOpptjeningDto(AnnenInntekt annenInntekt) {
            this.type = annenInntekt.type;
            this.periode.fom = annenInntekt.tidsperiode.startdato;
            this.periode.tom = annenInntekt.tidsperiode.sluttdato;
        }
    }
}
