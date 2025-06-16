package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.MorsAktivitet;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.Oppholdsårsak;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.Overføringsårsak;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UtsettelsesÅrsak;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.uttaksplan.KontoType;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.uttaksplan.OppholdsPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.uttaksplan.OverføringsPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.uttaksplan.UtsettelsesPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.uttaksplan.UttaksPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.uttaksplan.Uttaksplanperiode;


public class UttakplanPeriodeBuilder {

    public static UttaksperiodeBuilder uttak(StønadskontoType konto, LocalDate fom, LocalDate tom) {
        return new UttaksperiodeBuilder(tilKontoType(konto), fom, tom);
    }

    public static GradertUttaksperiodeBuilder gradert(StønadskontoType konto, LocalDate fom, LocalDate tom, Double stillingsprosent) {
        return new GradertUttaksperiodeBuilder(tilKontoType(konto), fom, tom, stillingsprosent);
    }

    public static OppholdsPeriodeBuilder opphold(Oppholdsårsak oppholdsårsak, LocalDate fom, LocalDate tom) {
        return new OppholdsPeriodeBuilder(fom, tom, oppholdsårsak);
    }

    public static OverføringsperioderBuilder overføring(Overføringsårsak årsak, StønadskontoType konto, LocalDate fom, LocalDate tom) {
        return new OverføringsperioderBuilder(tilKontoType(konto), fom, tom, årsak);
    }

    public static UtsettelsePeriodeBuilder utsettelse(UtsettelsesÅrsak årsak, LocalDate fom, LocalDate tom) {
        return new UtsettelsePeriodeBuilder(fom, tom, årsak);
    }

    public static UtsettelsePeriodeBuilder friUtsettelse(LocalDate fom, LocalDate tom) {
        return new UtsettelsePeriodeBuilder(fom, tom, UtsettelsesÅrsak.FRI);
    }


    public static class UttaksperiodeBuilder {
        private final LocalDate fom;
        private final LocalDate tom;
        private final KontoType konto;
        private MorsAktivitet morsAktivitetIPerioden;
        private Boolean ønskerSamtidigUttak;
        private Boolean ønskerFlerbarnsdager;
        private Double samtidigUttakProsent;

        public UttaksperiodeBuilder(KontoType konto, LocalDate fom, LocalDate tom) {
            this.fom = fom;
            this.tom = tom;
            this.konto = konto;
        }

        public UttaksperiodeBuilder medMorsAktivitetIPerioden(MorsAktivitet morsAktivitetIPerioden) {
            this.morsAktivitetIPerioden = morsAktivitetIPerioden;
            return this;
        }

        public UttaksperiodeBuilder medØnskerSamtidigUttak(Boolean ønskerSamtidigUttak) {
            this.ønskerSamtidigUttak = ønskerSamtidigUttak;
            return this;
        }

        public UttaksperiodeBuilder medØnskerFlerbarnsdager(Boolean ønskerFlerbarnsdager) {
            this.ønskerFlerbarnsdager = ønskerFlerbarnsdager;
            return this;
        }

        public UttaksperiodeBuilder medSamtidigUttakProsent(Double samtidigUttakProsent) {
            this.samtidigUttakProsent = samtidigUttakProsent;
            return this;
        }


        public Uttaksplanperiode build() {
            return new UttaksPeriodeDto(fom,
                tom,
                konto,
                morsAktivitetIPerioden,
                ønskerSamtidigUttak,
                ønskerFlerbarnsdager,
                samtidigUttakProsent,
                null);
        }
    }

    public static class GradertUttaksperiodeBuilder {
        private final LocalDate fom;
        private final LocalDate tom;
        private final KontoType konto;
        private MorsAktivitet morsAktivitetIPerioden;
        private Boolean ønskerSamtidigUttak;
        private Boolean ønskerFlerbarnsdager;
        private Double samtidigUttakProsent;
        private final Double stillingsprosent;
        private Boolean erArbeidstaker;
        private Boolean erFrilanser;
        private Boolean erSelvstendig;
        private List<String> orgnumre;

        public GradertUttaksperiodeBuilder(KontoType konto, LocalDate fom, LocalDate tom, Double stillingsprosent) {
            this.fom = fom;
            this.tom = tom;
            this.konto = konto;
            this.stillingsprosent = stillingsprosent;
        }

        public GradertUttaksperiodeBuilder medMorsAktivitetIPerioden(MorsAktivitet morsAktivitetIPerioden) {
            this.morsAktivitetIPerioden = morsAktivitetIPerioden;
            return this;
        }

        public GradertUttaksperiodeBuilder medØnskerSamtidigUttak(Boolean ønskerSamtidigUttak) {
            this.ønskerSamtidigUttak = ønskerSamtidigUttak;
            return this;
        }

        public GradertUttaksperiodeBuilder medØnskerFlerbarnsdager(Boolean ønskerFlerbarnsdager) {
            this.ønskerFlerbarnsdager = ønskerFlerbarnsdager;
            return this;
        }

        public GradertUttaksperiodeBuilder medSamtidigUttakProsent(Double samtidigUttakProsent) {
            this.samtidigUttakProsent = samtidigUttakProsent;
            return this;
        }

        public GradertUttaksperiodeBuilder medErArbeidstaker(Boolean erArbeidstaker) {
            this.erArbeidstaker = erArbeidstaker;
            return this;
        }

        public GradertUttaksperiodeBuilder medErFrilanser(Boolean erFrilanser) {
            this.erFrilanser = erFrilanser;
            return this;
        }

        public GradertUttaksperiodeBuilder medErSelvstendig(Boolean erSelvstendig) {
            this.erSelvstendig = erSelvstendig;
            return this;
        }

        public GradertUttaksperiodeBuilder medOrgnumre(List<String> orgnumre) {
            this.orgnumre = orgnumre;
            return this;
        }

        public UttaksPeriodeDto build() {
            return new UttaksPeriodeDto(fom,
                tom,
                konto,
                morsAktivitetIPerioden,
                ønskerSamtidigUttak,
                ønskerFlerbarnsdager,
                samtidigUttakProsent,
                new UttaksPeriodeDto.GraderingDto(stillingsprosent, erArbeidstaker, erFrilanser, erSelvstendig, orgnumre));
        }
    }

    public static class OverføringsperioderBuilder {
        private final LocalDate fom;
        private final LocalDate tom;
        private final Overføringsårsak årsak;
        private final KontoType konto;

        public OverføringsperioderBuilder(KontoType konto, LocalDate fom, LocalDate tom, Overføringsårsak årsak) {
            this.fom = fom;
            this.tom = tom;
            this.årsak = årsak;
            this.konto = konto;
        }

        public Uttaksplanperiode build() {
            return new OverføringsPeriodeDto(fom, tom, årsak, konto);
        }
    }


    public static class OppholdsPeriodeBuilder {
        private final LocalDate fom;
        private final LocalDate tom;
        private final Oppholdsårsak årsak;

        public OppholdsPeriodeBuilder(LocalDate fom, LocalDate tom, Oppholdsårsak årsak) {
            this.fom = fom;
            this.tom = tom;
            this.årsak = årsak;
        }

        public Uttaksplanperiode build() {
            return new OppholdsPeriodeDto(fom, tom, årsak);
        }
    }


    public static class UtsettelsePeriodeBuilder {
        private final LocalDate fom;
        private final LocalDate tom;
        private final UtsettelsesÅrsak årsak;
        private MorsAktivitet morsAktivitetIPerioden;
        private boolean erArbeidstaker;

        public UtsettelsePeriodeBuilder(LocalDate fom, LocalDate tom, UtsettelsesÅrsak årsak) {
            this.fom = fom;
            this.tom = tom;
            this.årsak = årsak;
        }

        public UtsettelsePeriodeBuilder medMorsAktivitetIPerioden(MorsAktivitet morsAktivitetIPerioden) {
            this.morsAktivitetIPerioden = morsAktivitetIPerioden;
            return this;
        }

        public UtsettelsePeriodeBuilder medErArbeidstaker(boolean erArbeidstaker) {
            this.erArbeidstaker = erArbeidstaker;
            return this;
        }

        public Uttaksplanperiode build() {
            return new UtsettelsesPeriodeDto(fom, tom, årsak, morsAktivitetIPerioden, erArbeidstaker);
        }
    }

    private static KontoType tilKontoType(StønadskontoType konto) {
        return switch (konto) {
            case IKKE_SATT -> null;
            case FELLESPERIODE -> KontoType.FELLESPERIODE;
            case MØDREKVOTE -> KontoType.MØDREKVOTE;
            case FEDREKVOTE -> KontoType.FEDREKVOTE;
            case FORELDREPENGER -> KontoType.FORELDREPENGER;
            case FORELDREPENGER_FØR_FØDSEL -> KontoType.FORELDREPENGER_FØR_FØDSEL;
        };
    }
}
