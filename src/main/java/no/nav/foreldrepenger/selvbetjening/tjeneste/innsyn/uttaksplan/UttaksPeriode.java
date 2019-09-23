package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.uttaksplan;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.LukketPeriode;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.PeriodeResultatType;

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
    private final Integer utbetalingprosent;
    private final Boolean gjelderAnnenPart;
    private final MorsAktivitet morsAktivitet;
    private final Boolean flerbarnsdager;
    private final Boolean manueltBehandlet;
    private final Integer samtidigUttaksprosent;
    private final UttakArbeidType uttakArbeidType;
    private final ArbeidsgiverInfo arbeidsgiverInfo;

    public UttaksPeriode(
            @JsonProperty("oppholdAarsak") OppholdÅrsak oppholdAarsak,
            @JsonProperty("overfoeringAarsak") OverføringÅrsak overfoeringAarsak,
            @JsonProperty("graderingAvslagAarsak") GraderingAvslagÅrsak graderingAvslagAarsak,
            @JsonProperty("utsettelsePeriodeType") UtsettelsePeriodeType utsettelsePeriodeType,
            @JsonProperty("periodeResultatType") PeriodeResultatType periodeResultatType,
            @JsonProperty("graderingInnvilget") Boolean graderingInnvilget,
            @JsonProperty("samtidigUttak") Boolean samtidigUttak,
            @JsonProperty("fom") LocalDate fom, @JsonProperty("tom") LocalDate tom,
            @JsonProperty("stønadskontotype") @JsonAlias("trekkonto") StønadskontoType stønadskontotype,
            @JsonProperty("trekkDager") Double trekkDager,
            @JsonProperty("arbeidstidprosent") Integer arbeidstidProsent,
            @JsonProperty("utbetalingprosent") Integer utbetalingprosent,
            @JsonProperty("gjelderAnnenPart") Boolean gjelderAnnenPart,
            @JsonProperty("manueltBehandlet") Boolean manueltBehandlet,
            @JsonProperty("samtidigUttaksprosent") Integer samtidigUttaksprosent,
            @JsonProperty("morsAktivitet") MorsAktivitet morsAktivitet,
            @JsonProperty("flerbarnsdager") Boolean flerbarnsdager,
            @JsonProperty("uttakArbeidType") UttakArbeidType uttakArbeidType,
            @JsonProperty("arbeidsgiverInfo") ArbeidsgiverInfo arbeidsgiverInfo) {
        this.oppholdAarsak = oppholdAarsak;
        this.overfoeringAarsak = overfoeringAarsak;
        this.utsettelsePeriodeType = utsettelsePeriodeType;
        this.periodeResultatType = periodeResultatType;
        this.graderingInnvilget = graderingInnvilget;
        this.samtidigUttak = samtidigUttak;
        this.fom = fom;
        this.tom = tom;
        this.periode = new LukketPeriode(fom, tom);
        this.stønadskontotype = stønadskontotype;
        this.trekkDager = trekkDager;
        this.arbeidstidProsent = arbeidstidProsent;
        this.utbetalingprosent = utbetalingprosent;
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

    public Integer getUtbetalingprosent() {
        return utbetalingprosent;
    }

    public Boolean getGjelderAnnenPart() {
        return gjelderAnnenPart;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gjelderAnnenPart, utbetalingprosent, arbeidstidProsent, trekkDager, stønadskontotype,
                periode, samtidigUttak, graderingInnvilget, periodeResultatType, oppholdAarsak, overfoeringAarsak, fom,
                tom, uttakArbeidType, arbeidsgiverInfo);
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
                && Objects.equals(this.utbetalingprosent, that.utbetalingprosent)
                && Objects.equals(this.overfoeringAarsak, that.overfoeringAarsak)
                && Objects.equals(this.oppholdAarsak, that.oppholdAarsak)
                && Objects.equals(this.arbeidstidProsent, that.arbeidstidProsent)
                && Objects.equals(this.trekkDager, that.trekkDager)
                && Objects.equals(this.stønadskontotype, that.stønadskontotype)
                && Objects.equals(this.periode, that.periode)
                && Objects.equals(this.samtidigUttak, that.samtidigUttak)
                && Objects.equals(this.tom, that.tom)
                && Objects.equals(this.fom, that.fom)
                && Objects.equals(this.uttakArbeidType, that.uttakArbeidType)
                && Objects.equals(this.arbeidsgiverInfo, that.arbeidsgiverInfo)
                && Objects.equals(this.graderingInnvilget, that.graderingInnvilget);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [oppholdAarsak=" + oppholdAarsak + ", overfoeringAarsak="
                + overfoeringAarsak
                + ", utsettelsePeriodeType=" + utsettelsePeriodeType + ", periodeResultatType=" + periodeResultatType
                + ", graderingInnvilget=" + graderingInnvilget + ", samtidigUttak=" + samtidigUttak + ", periode="
                + periode + ", stønadskontotype=" + stønadskontotype + ", trekkDager=" + trekkDager
                + ", arbeidstidProsent=" + arbeidstidProsent + ", utbetalingprosent=" + utbetalingprosent
                + ", gjelderAnnenPart=" + gjelderAnnenPart
                + ", uttakArbeidType=" + uttakArbeidType
                + ", arbeidsgiverInfo=" + arbeidsgiverInfo
                + ", fom=" + fom
                + ", tom=" + tom + "]";
    }
}
