package no.nav.foreldrepenger.selvbetjening.innsyn.uttaksplan;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.selvbetjening.innsyn.LukketPeriode;
import no.nav.foreldrepenger.selvbetjening.innsyn.PeriodeResultatType;

public class UttaksPeriode {
    private final OppholdÅrsak oppholdAarsak;
    private final OverføringÅrsak overfoeringAarsak;
    private final GraderingAvslagÅrsak graderingAvslagAarsak;
    private final UtsettelsePeriodeType utsettelsePeriodeType;
    private final PeriodeResultatType periodeResultatType;
    private final Boolean graderingInnvilget;
    private final Boolean samtidigUttak;
    private final LocalDate fom;
    private final LocalDate tom;
    @Valid
    private final LukketPeriode periode;
    private final StønadskontoType stønadskontotype;
    private final Double trekkDager;
    private final Integer arbeidstidProsent;
    private final Integer utbetalingsprosent;
    private final Boolean gjelderAnnenPart;
    private final MorsAktivitet morsAktivitet;
    private final Boolean flerbarnsdager;
    private final Boolean manueltBehandlet;
    private final Integer samtidigUttaksprosent;
    private final UttakArbeidType uttakArbeidType;
    private final ArbeidsgiverInfo arbeidsgiverInfo;
    private final String periodeResultatÅrsak;

    public UttaksPeriode(@JsonProperty("oppholdAarsak") OppholdÅrsak oppholdAarsak,
                         @JsonProperty("overfoeringAarsak") OverføringÅrsak overfoeringAarsak,
                         @JsonProperty("graderingAvslagAarsak") GraderingAvslagÅrsak graderingAvslagAarsak,
                         @JsonProperty("utsettelsePeriodeType") UtsettelsePeriodeType utsettelsePeriodeType,
                         @JsonProperty("periodeResultatType") PeriodeResultatType periodeResultatType,
                         @JsonProperty("graderingInnvilget") Boolean graderingInnvilget,
                         @JsonProperty("samtidigUttak") Boolean samtidigUttak,
                         @JsonProperty("fom") LocalDate fom,
                         @JsonProperty("tom") LocalDate tom,
                         @JsonProperty("stønadskontotype") @JsonAlias("trekkonto") StønadskontoType stønadskontotype,
                         @JsonProperty("trekkDager") Double trekkDager,
                         @JsonProperty("arbeidstidprosent") Integer arbeidstidProsent,
                         @JsonProperty("utbetalingsprosent") Integer utbetalingsprosent,
                         @JsonProperty("gjelderAnnenPart") Boolean gjelderAnnenPart,
                         @JsonProperty("manueltBehandlet") Boolean manueltBehandlet,
                         @JsonProperty("samtidigUttaksprosent") Integer samtidigUttaksprosent,
                         @JsonProperty("morsAktivitet") MorsAktivitet morsAktivitet,
                         @JsonProperty("flerbarnsdager") Boolean flerbarnsdager,
                         @JsonProperty("uttakArbeidType") UttakArbeidType uttakArbeidType,
                         @JsonProperty("arbeidsgiverInfo") ArbeidsgiverInfo arbeidsgiverInfo,
                         @JsonProperty("periodeResultatÅrsak") String periodeResultatÅrsak) {
        this.oppholdAarsak = oppholdAarsak;
        this.overfoeringAarsak = overfoeringAarsak;
        this.utsettelsePeriodeType = utsettelsePeriodeType;
        this.periodeResultatType = periodeResultatType;
        this.graderingInnvilget = graderingInnvilget;
        this.samtidigUttak = samtidigUttak;
        this.fom = fom;
        this.tom = tom;
        this.periodeResultatÅrsak = periodeResultatÅrsak;
        this.periode = new LukketPeriode(fom, tom);
        this.stønadskontotype = stønadskontotype;
        this.trekkDager = trekkDager;
        this.arbeidstidProsent = arbeidstidProsent;
        this.utbetalingsprosent = utbetalingsprosent;
        this.gjelderAnnenPart = gjelderAnnenPart;
        this.graderingAvslagAarsak = graderingAvslagAarsak;
        this.manueltBehandlet = manueltBehandlet;
        this.samtidigUttaksprosent = samtidigUttaksprosent;
        this.morsAktivitet = morsAktivitet;
        this.flerbarnsdager = flerbarnsdager;
        this.uttakArbeidType = uttakArbeidType;
        this.arbeidsgiverInfo = arbeidsgiverInfo;
    }

    public UttakArbeidType getUttakArbeidType() {
        return uttakArbeidType;
    }

    public ArbeidsgiverInfo getArbeidsgiverInfo() {
        return arbeidsgiverInfo;
    }

    public LocalDate getFom() {
        return fom;
    }

    public LocalDate getTom() {
        return tom;
    }

    public Boolean getFlerbarnsdager() {
        return flerbarnsdager;
    }

    public Boolean getManueltBehandlet() {
        return manueltBehandlet;
    }

    public Integer getSamtidigUttaksprosent() {
        return samtidigUttaksprosent;
    }

    public OppholdÅrsak getOppholdAarsak() {
        return oppholdAarsak;
    }

    public OverføringÅrsak getOverfoeringAarsak() {
        return overfoeringAarsak;
    }

    public GraderingAvslagÅrsak getGraderingAvslagAarsak() {
        return graderingAvslagAarsak;
    }

    public UtsettelsePeriodeType getUtsettelsePeriodeType() {
        return utsettelsePeriodeType;
    }

    public PeriodeResultatType getPeriodeResultatType() {
        return periodeResultatType;
    }

    public Boolean getGraderingInnvilget() {
        return graderingInnvilget;
    }

    public Boolean getSamtidigUttak() {
        return samtidigUttak;
    }

    public LukketPeriode getPeriode() {
        return periode;
    }

    public MorsAktivitet getMorsAktivitet() {
        return morsAktivitet;
    }

    public StønadskontoType getStønadskontotype() {
        return stønadskontotype;
    }

    public Double getTrekkDager() {
        return trekkDager;
    }

    public Integer getArbeidstidProsent() {
        return arbeidstidProsent;
    }

    public Integer getUtbetalingsprosent() {
        return utbetalingsprosent;
    }

    public Boolean getGjelderAnnenPart() {
        return gjelderAnnenPart;
    }

    public String getPeriodeResultatÅrsak() {
        return periodeResultatÅrsak;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gjelderAnnenPart, utbetalingsprosent, arbeidstidProsent, trekkDager, stønadskontotype,
                periode, samtidigUttak, graderingInnvilget, periodeResultatType, oppholdAarsak, overfoeringAarsak, fom,
                tom, uttakArbeidType, arbeidsgiverInfo, periodeResultatÅrsak);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        UttaksPeriode that = (UttaksPeriode) o;
        return Objects.equals(gjelderAnnenPart, that.gjelderAnnenPart)
                && Objects.equals(this.utbetalingsprosent, that.utbetalingsprosent)
                && Objects.equals(this.overfoeringAarsak, that.overfoeringAarsak)
                && Objects.equals(this.oppholdAarsak, that.oppholdAarsak)
                && Objects.equals(this.arbeidstidProsent, that.arbeidstidProsent)
                && Objects.equals(this.trekkDager, that.trekkDager)
                && Objects.equals(this.stønadskontotype, that.stønadskontotype)
                && Objects.equals(this.periode, that.periode) && Objects.equals(this.samtidigUttak, that.samtidigUttak)
                && Objects.equals(this.tom, that.tom) && Objects.equals(this.fom, that.fom)
                && Objects.equals(this.uttakArbeidType, that.uttakArbeidType)
                && Objects.equals(this.arbeidsgiverInfo, that.arbeidsgiverInfo)
                && Objects.equals(this.periodeResultatÅrsak, that.periodeResultatÅrsak)
                && Objects.equals(this.graderingInnvilget, that.graderingInnvilget);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [oppholdAarsak=" + oppholdAarsak + ", overfoeringAarsak="
                + overfoeringAarsak + ", utsettelsePeriodeType=" + utsettelsePeriodeType + ", periodeResultatType="
                + periodeResultatType + ", graderingInnvilget=" + graderingInnvilget + ", samtidigUttak="
                + samtidigUttak + ", periode=" + periode + ", stønadskontotype=" + stønadskontotype + ", trekkDager="
                + trekkDager + ", arbeidstidProsent=" + arbeidstidProsent + ", utbetalingsprosent=" + utbetalingsprosent
                + ", gjelderAnnenPart=" + gjelderAnnenPart + ", uttakArbeidType=" + uttakArbeidType  + ", periodeResultatÅrsak=" + periodeResultatÅrsak
                + ", arbeidsgiverInfo=" + arbeidsgiverInfo + ", fom=" + fom + ", tom=" + tom + "]";
    }
}
