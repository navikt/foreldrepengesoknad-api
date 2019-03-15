package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak.uttak;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.LukketPeriode;

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

    public UttaksPeriode(LukketPeriode periode, UttaksPeriodeResultatType resultatType,
            UttaksPeriodeResultatÅrsak årsak, String begrunnelse, List<PeriodeAktivitet> periodeAktiviteter,
            Boolean graderingInnvilget, Boolean samtidigUttak, Boolean manueltBehandlet,
            ManuellBehandlingsÅrsak manuellBehandlingsårsak) {
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [periode=" + periode + ", resultatType=" + resultatType + ", årsak="
                + årsak
                + ", begrunnelse=" + begrunnelse + ", periodeAktiviteter=" + periodeAktiviteter
                + ", graderingInnvilget=" + graderingInnvilget + ", samtidigUttak=" + samtidigUttak
                + ", manueltBehandlet=" + manueltBehandlet + ", manuellBehandlingsårsak=" + manuellBehandlingsårsak
                + "]";
    }

}
