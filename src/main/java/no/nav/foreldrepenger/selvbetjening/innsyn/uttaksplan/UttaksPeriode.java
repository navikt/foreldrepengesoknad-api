package no.nav.foreldrepenger.selvbetjening.innsyn.uttaksplan;

import java.time.LocalDate;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAlias;

record UttaksPeriode(OppholdÅrsak oppholdAarsak,
                     OverføringÅrsak overfoeringAarsak,
                     GraderingAvslagÅrsak graderingAvslagAarsak,
                     UtsettelsePeriodeType utsettelsePeriodeType,
                     PeriodeResultatType periodeResultatType,
                     Boolean graderingInnvilget,
                     Boolean samtidigUttak,
                     LocalDate fom,
                     LocalDate tom,
                     @Valid LukketPeriode periode,
                     @JsonAlias("trekkonto") StønadskontoType stønadskontotype,
                     Double trekkDager,
                     Integer arbeidstidprosent,
                     Integer utbetalingsprosent,
                     Boolean gjelderAnnenPart,
                     MorsAktivitet morsAktivitet,
                     Boolean flerbarnsdager,
                     Boolean manueltBehandlet,
                     Integer samtidigUttaksprosent,
                     UttakArbeidType uttakArbeidType,
                     ArbeidsgiverInfo arbeidsgiverInfo,
                     String periodeResultatÅrsak) {

    public UttaksPeriode {
        periode = new LukketPeriode(fom, tom);
    }
}
