package no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2;

import java.time.LocalDate;

record VedtakPeriode(LocalDate fom,
                     LocalDate tom,
                     KontoType kontoType,
                     VedtakPeriodeResultat resultat,
                     UtsettelseÅrsak utsettelseÅrsak,
                     OppholdÅrsak oppholdÅrsak,
                     OverføringÅrsak overføringÅrsak,
                     Gradering gradering,
                     MorsAktivitet morsAktivitet,
                     SamtidigUttak samtidigUttak,
                     boolean flerbarnsdager) {
}
