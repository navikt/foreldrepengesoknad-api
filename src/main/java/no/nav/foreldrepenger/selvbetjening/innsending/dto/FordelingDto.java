package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.apache.commons.lang3.BooleanUtils.isTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.UttaksplanPeriode;

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
        public List<String> virksomhetsnummer;
        public Double arbeidstidProsent;
        public Boolean ønskerSamtidigUttak;
        public Boolean erArbeidstaker;
        public Boolean frilans;
        public Boolean selvstendig;
        public Boolean arbeidsForholdSomskalGraderes;
        public String morsAktivitetsType;
        public Double samtidigUttakProsent;
        public List<String> vedlegg;
        public Boolean ønskerFlerbarnsdager;

        public FordelingPeriodeDto(UttaksplanPeriode u) {
            if (isTrue(u.getGradert())) {
                this.type = "gradert";
                this.arbeidsForholdSomskalGraderes = true;
            } else {
                this.type = u.getType();
            }
            this.ønskerFlerbarnsdager = u.ønskerFlerbarnsdager;
            this.uttaksperiodeType = u.getKonto();
            this.fom = u.getTidsperiode().fom();
            this.tom = u.getTidsperiode().tom();
            this.samtidigUttakProsent = u.getSamtidigUttakProsent();
            this.årsak = u.getÅrsak();
            this.virksomhetsnummer = u.getOrgnumre();
            this.arbeidstidProsent = u.getStillingsprosent();
            this.ønskerSamtidigUttak = u.getØnskerSamtidigUttak();
            this.erArbeidstaker = u.getErArbeidstaker();
            this.frilans = u.getErFrilanser();
            this.selvstendig = u.getErSelvstendig();
            this.morsAktivitetsType = u.getMorsAktivitetIPerioden();
            this.vedlegg = u.getVedlegg();
        }
    }
}
