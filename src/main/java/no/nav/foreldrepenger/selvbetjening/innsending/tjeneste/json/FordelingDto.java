package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.innsending.json.UttaksplanPeriode;

public class FordelingDto {

    public List<FordelingPeriodeDto> perioder = new ArrayList<>();
    public Boolean erAnnenForelderInformert;

    public FordelingDto(List<UttaksplanPeriode> uttaksplanperioder, Boolean annenForelderErInformert) {
        for (UttaksplanPeriode u : uttaksplanperioder) {
            perioder.add(new FordelingPeriodeDto(u));
        }
        this.erAnnenForelderInformert = annenForelderErInformert;
    }

    @JsonInclude(NON_NULL)
    public class FordelingPeriodeDto {

        public String type;
        public LocalDate fom;
        public LocalDate tom;
        public String årsak;
        public String uttaksperiodeType;
        public String virksomhetsNummer;
        public String arbeidstidProsent;
        public Boolean ønskerSamtidigUttak;
        public Boolean erArbeidstaker;
        public Boolean arbeidsForholdSomskalGraderes;
        public String morsAktivitetsType;
        public List<String> vedlegg;
        public Boolean ønskerFlerbarnsdager;

        public FordelingPeriodeDto(UttaksplanPeriode u) {
            if (u.gradert != null) {
                this.type = u.gradert ? "gradert" : u.type;
                this.arbeidsForholdSomskalGraderes = u.gradert ? true : null;
            } else {
                this.type = u.type;
            }
            this.ønskerFlerbarnsdager = u.ønskerFlerbarnsdager;
            this.uttaksperiodeType = u.konto;
            this.fom = u.tidsperiode.fom;
            this.tom = u.tidsperiode.tom;
            this.årsak = u.årsak;
            this.virksomhetsNummer = u.orgnr;
            this.arbeidstidProsent = u.stillingsprosent;
            this.ønskerSamtidigUttak = u.ønskerSamtidigUttak;
            this.erArbeidstaker = u.erArbeidstaker;
            this.morsAktivitetsType = u.morsAktivitetIPerioden;
            this.vedlegg = u.vedlegg;
        }
    }
}
