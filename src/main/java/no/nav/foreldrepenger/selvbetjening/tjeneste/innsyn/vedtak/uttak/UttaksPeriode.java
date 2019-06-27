package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak.uttak;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.LukketPeriode;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.uttaksplan.MorsAktivitet;

public class UttaksPeriode {

    private final LukketPeriode periode;
    private final UttaksPeriodeResultatType resultatType;
    private final UttaksPeriodeResultatÅrsak årsak;
    private final String begrunnelse;
    private final List<PeriodeAktivitet> periodeAktiviteter;
    private final Boolean graderingInnvilget;
    private final Boolean samtidigUttak;
    private final Boolean manueltBehandlet;
    private final ManuellBehandlingsÅrsak manuellBehandlingsårsak;

    @JsonCreator
    public UttaksPeriode(@JsonProperty("periode") LukketPeriode periode,
            @JsonProperty("resultatType") UttaksPeriodeResultatType resultatType,
            @JsonProperty("årsak") UttaksPeriodeResultatÅrsak årsak,
            @JsonProperty("begrunnelse") String begrunnelse,
            @JsonProperty("periodeAktiviteter") List<PeriodeAktivitet> periodeAktiviteter,
            @JsonProperty("graderingInnvilget") Boolean graderingInnvilget,
            @JsonProperty("samtidigUttak") Boolean samtidigUttak,
            @JsonProperty("manueltBehandlet") Boolean manueltBehandlet,
            @JsonProperty("manuellBehandlingsårsak") ManuellBehandlingsÅrsak manuellBehandlingsårsak
    ) {
        this.periode = periode;
        this.resultatType = resultatType;
        this.årsak = årsak;
        this.begrunnelse = begrunnelse;
        this.periodeAktiviteter = periodeAktiviteter;
        this.graderingInnvilget = graderingInnvilget;
        this.samtidigUttak = samtidigUttak;
        this.manueltBehandlet = manueltBehandlet;
        this.manuellBehandlingsårsak = manuellBehandlingsårsak;
    }

    public LukketPeriode getPeriode() {
        return periode;
    }

    public UttaksPeriodeResultatType getResultatType() {
        return resultatType;
    }

    public UttaksPeriodeResultatÅrsak getÅrsak() {
        return årsak;
    }

    public String getBegrunnelse() {
        return begrunnelse;
    }

    public List<PeriodeAktivitet> getPeriodeAktiviteter() {
        return periodeAktiviteter;
    }

    public Boolean getGraderingInnvilget() {
        return graderingInnvilget;
    }

    public Boolean getSamtidigUttak() {
        return samtidigUttak;
    }

    public Boolean getManueltBehandlet() {
        return manueltBehandlet;
    }

    public ManuellBehandlingsÅrsak getManuellBehandlingsårsak() {
        return manuellBehandlingsårsak;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [periode=" + periode + ", resultatType=" + resultatType + ", årsak="
                + årsak
                + ", begrunnelse=" + begrunnelse + ", periodeAktiviteter=" + periodeAktiviteter
                + ", graderingInnvilget=" + graderingInnvilget + ", samtidigUttak=" + samtidigUttak
                + ", manueltBehandlet=" + manueltBehandlet + ", manuellBehandlingsårsak=" + manuellBehandlingsårsak;
    }

}
