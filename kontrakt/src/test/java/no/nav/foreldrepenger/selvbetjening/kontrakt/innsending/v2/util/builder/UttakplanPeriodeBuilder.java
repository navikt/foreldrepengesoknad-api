package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.MorsAktivitet;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.Oppholdsårsak;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.Overføringsårsak;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UtsettelsesÅrsak;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.GradertUttaksPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.KontoType;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.OppholdsPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.OverføringsPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.UtsettelsesPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.UttaksPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.Uttaksplanperiode;


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
        return new UtsettelsePeriodeBuilder(UtsettelsesPeriodeDto.Type.UTSETTELSE, fom, tom, årsak);
    }

    public static UtsettelsePeriodeBuilder friUtsettelse(LocalDate fom, LocalDate tom) {
        return new UtsettelsePeriodeBuilder(UtsettelsesPeriodeDto.Type.FRI, fom, tom, null);
    }


    public static class UttaksperiodeBuilder {
        private final LocalDate fom;
        private final LocalDate tom;
        private final KontoType konto;
        private MorsAktivitet morsAktivitetIPerioden;
        private Boolean ønskerSamtidigUttak;
        private Boolean justeresVedFødsel;
        private Boolean ønskerFlerbarnsdager;
        private Double samtidigUttakProsent;
        private List<MutableVedleggReferanseDto> vedleggsreferanser;

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

        public UttaksperiodeBuilder medJusteresVedFødsel(Boolean justeresVedFødsel) {
            this.justeresVedFødsel = justeresVedFødsel;
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

        public UttaksperiodeBuilder medVedleggsreferanser(List<MutableVedleggReferanseDto> vedleggsreferanser) {
            this.vedleggsreferanser = vedleggsreferanser;
            return this;
        }

        public Uttaksplanperiode build() {
            return new UttaksPeriodeDto(
                fom,
                tom,
                konto,
                morsAktivitetIPerioden,
                ønskerSamtidigUttak,
                justeresVedFødsel,
                ønskerFlerbarnsdager,
                samtidigUttakProsent,
                vedleggsreferanser
            );
        }
    }

    public static class GradertUttaksperiodeBuilder {
        private final LocalDate fom;
        private final LocalDate tom;
        private final KontoType konto;
        private MorsAktivitet morsAktivitetIPerioden;
        private Boolean ønskerSamtidigUttak;
        private Boolean justeresVedFødsel;
        private Boolean ønskerFlerbarnsdager;
        private Double samtidigUttakProsent;
        private final  Double stillingsprosent;
        private Boolean erArbeidstaker;
        private Boolean erFrilanser;
        private Boolean erSelvstendig;
        private List<String> orgnumre;
        private List<MutableVedleggReferanseDto> vedleggsreferanser;

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

        public GradertUttaksperiodeBuilder medJusteresVedFødsel(Boolean justeresVedFødsel) {
            this.justeresVedFødsel = justeresVedFødsel;
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

        public GradertUttaksperiodeBuilder medVedleggsreferanser(List<MutableVedleggReferanseDto> vedleggsreferanser) {
            this.vedleggsreferanser = vedleggsreferanser;
            return this;
        }

        public Uttaksplanperiode build() {
            return new GradertUttaksPeriodeDto(
                fom,
                tom,
                konto,
                morsAktivitetIPerioden,
                ønskerSamtidigUttak,
                justeresVedFødsel,
                ønskerFlerbarnsdager,
                samtidigUttakProsent,
                stillingsprosent,
                erArbeidstaker,
                erFrilanser,
                erSelvstendig,
                orgnumre,
                vedleggsreferanser
            );
        }
    }

    public static class OverføringsperioderBuilder {
        private final LocalDate fom;
        private final LocalDate tom;
        private final Overføringsårsak årsak;
        private final KontoType konto;
        private List<MutableVedleggReferanseDto> vedleggsreferanser;

        public OverføringsperioderBuilder(KontoType konto, LocalDate fom, LocalDate tom, Overføringsårsak årsak) {
            this.fom = fom;
            this.tom = tom;
            this.årsak = årsak;
            this.konto = konto;
        }

        public OverføringsperioderBuilder medVedleggsreferanser(List<MutableVedleggReferanseDto> vedleggsreferanser) {
            this.vedleggsreferanser = vedleggsreferanser;
            return this;
        }

        public Uttaksplanperiode build() {
            return new OverføringsPeriodeDto(
                fom,
                tom,
                årsak,
                konto,
                vedleggsreferanser
            );
        }
    }


    public static class OppholdsPeriodeBuilder {
        private final LocalDate fom;
        private final LocalDate tom;
        private final Oppholdsårsak årsak;
        private List<MutableVedleggReferanseDto> vedleggsreferanser;

        public OppholdsPeriodeBuilder(LocalDate fom, LocalDate tom, Oppholdsårsak årsak) {
            this.fom = fom;
            this.tom = tom;
            this.årsak = årsak;
        }

        public OppholdsPeriodeBuilder medVedleggsreferanser(List<MutableVedleggReferanseDto> vedleggsreferanser) {
            this.vedleggsreferanser = vedleggsreferanser;
            return this;
        }

        public Uttaksplanperiode build() {
            return new OppholdsPeriodeDto(
                fom,
                tom,
                årsak,
                vedleggsreferanser
            );
        }
    }


    public static class UtsettelsePeriodeBuilder {
        private final UtsettelsesPeriodeDto.Type type;
        private final LocalDate fom;
        private final LocalDate tom;
        private final UtsettelsesÅrsak årsak;
        private MorsAktivitet morsAktivitetIPerioden;
        private Boolean erArbeidstaker;
        private List<MutableVedleggReferanseDto> vedleggsreferanser;

        public UtsettelsePeriodeBuilder(UtsettelsesPeriodeDto.Type type, LocalDate fom, LocalDate tom, UtsettelsesÅrsak årsak) {
            this.type = type;
            this.fom = fom;
            this.tom = tom;
            this.årsak = årsak;
        }

        public UtsettelsePeriodeBuilder medMorsAktivitetIPerioden(MorsAktivitet morsAktivitetIPerioden) {
            this.morsAktivitetIPerioden = morsAktivitetIPerioden;
            return this;
        }

        public UtsettelsePeriodeBuilder medErArbeidstaker(Boolean erArbeidstaker) {
            this.erArbeidstaker = erArbeidstaker;
            return this;
        }

        public UtsettelsePeriodeBuilder medVedleggsreferanser(List<MutableVedleggReferanseDto> vedleggsreferanser) {
            this.vedleggsreferanser = vedleggsreferanser;
            return this;
        }

        public Uttaksplanperiode build() {
            return new UtsettelsesPeriodeDto(
                type,
                fom,
                tom,
                årsak,
                morsAktivitetIPerioden,
                erArbeidstaker,
                vedleggsreferanser
            );
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
