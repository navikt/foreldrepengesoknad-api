package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.uttaksplan;

import java.time.LocalDate;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.LukketPeriode;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.PeriodeResultatType;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.ProsentAndel;

public class UttaksPeriode {

    private final PeriodeResultatType periodeResultatType;
    private final Boolean graderingInnvilget;
    private final Boolean samtidigUttak;
    @Valid
    private final LukketPeriode periode;
    private final StønadskontoType stønadskontotype;
    private final Integer trekkDager;
    @Valid
    private final ProsentAndel arbeidstidProsent;
    @Valid
    private final ProsentAndel utbetalingprosent;
    private final Boolean gjelderAnnenPart;

    public UttaksPeriode(@JsonProperty("periodeResultatType") PeriodeResultatType periodeResultatType,
            @JsonProperty("graderingInnvilget") Boolean graderingInnvilget,
            @JsonProperty("samtidigUttak") Boolean samtidigUttak,
            @JsonProperty("fom") LocalDate fom, @JsonProperty("tom") LocalDate tom,
            @JsonProperty("stønadskontotype") @JsonAlias("trekkonto") StønadskontoType stønadskontotype,
            @JsonProperty("trekkDager") Integer trekkDager,
            @JsonProperty("arbeidstidprosent") ProsentAndel arbeidstidProsent,
            @JsonProperty("utbetalingprosent") ProsentAndel utbetalingprosent,
            @JsonProperty("gjelderAnnenPart") Boolean gjelderAnnenPart) {
        this.periodeResultatType = periodeResultatType;
        this.graderingInnvilget = graderingInnvilget;
        this.samtidigUttak = samtidigUttak;
        this.periode = new LukketPeriode(fom, tom);
        this.stønadskontotype = stønadskontotype;
        this.trekkDager = trekkDager;
        this.arbeidstidProsent = arbeidstidProsent;
        this.utbetalingprosent = utbetalingprosent;
        this.gjelderAnnenPart = gjelderAnnenPart;
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

    public StønadskontoType getStønadskontotype() {
        return stønadskontotype;
    }

    public Integer getTrekkDager() {
        return trekkDager;
    }

    public ProsentAndel getArbeidstidProsent() {
        return arbeidstidProsent;
    }

    public ProsentAndel getUtbetalingprosent() {
        return utbetalingprosent;
    }

    public Boolean getGjelderAnnenPart() {
        return gjelderAnnenPart;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [periodeResultatType=" + periodeResultatType + ", graderingInnvilget="
                + graderingInnvilget + ", samtidigUttak=" + samtidigUttak + ", periode=" + periode
                + ", stønadskontotype=" + stønadskontotype + ", trekkDager=" + trekkDager + ", arbeidstidProsent="
                + arbeidstidProsent + ", utbetalingprosent=" + utbetalingprosent + ", gjelderAnnenPart="
                + gjelderAnnenPart + "]";
    }

}
