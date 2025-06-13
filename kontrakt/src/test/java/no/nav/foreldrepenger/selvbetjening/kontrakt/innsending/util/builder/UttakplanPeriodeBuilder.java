package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.Oppholdsårsak;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.Overføringsårsak;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UtsettelsesÅrsak;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.UttaksplanPeriodeDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ÅpenPeriodeDtoOLD;

public class UttakplanPeriodeBuilder {
    UttaksplanPeriodeDtoOLD.UttakType type;
    ÅpenPeriodeDtoOLD tidsperiode;
    UttaksplanPeriodeDtoOLD.KontoType konto;
    String morsAktivitetIPerioden;
    String årsak;
    Double samtidigUttakProsent;
    Double stillingsprosent;
    boolean erArbeidstaker;
    boolean erFrilanser;
    boolean erSelvstendig;
    boolean gradert;
    boolean ønskerFlerbarnsdager;
    boolean ønskerSamtidigUttak;
    Boolean justeresVedFødsel;
    List<String> orgnumre;

    public static UttakplanPeriodeBuilder uttak(StønadskontoType konto, LocalDate fom, LocalDate tom) {
        return new UttakplanPeriodeBuilder(UttaksplanPeriodeDtoOLD.UttakType.UTTAK, konto, fom, tom);
    }

    public static UttakplanPeriodeBuilder gradert(StønadskontoType konto, LocalDate fom, LocalDate tom, Double stillingsprosent) {
        return new UttakplanPeriodeBuilder(UttaksplanPeriodeDtoOLD.UttakType.UTTAK, konto, fom, tom).medStillingsprosent(stillingsprosent)
            .medGradert(true);
    }

    public static UttakplanPeriodeBuilder opphold(Oppholdsårsak oppholdsårsak, LocalDate fom, LocalDate tom) {
        return new UttakplanPeriodeBuilder(UttaksplanPeriodeDtoOLD.UttakType.OPPHOLD, null, fom, tom).medÅrsak(oppholdsårsak.name());
    }

    public static UttakplanPeriodeBuilder overføring(Overføringsårsak årsak, StønadskontoType konto, LocalDate fom, LocalDate tom) {
        return new UttakplanPeriodeBuilder(UttaksplanPeriodeDtoOLD.UttakType.OVERFØRING, konto, fom, tom).medÅrsak(årsak.name());
    }

    public static UttakplanPeriodeBuilder utsettelse(UtsettelsesÅrsak årsak, LocalDate fom, LocalDate tom) {
        return new UttakplanPeriodeBuilder(UttaksplanPeriodeDtoOLD.UttakType.UTSETTELSE, null, fom, tom).medÅrsak(årsak.name());
    }

    public static UttakplanPeriodeBuilder friUtsettelse(LocalDate fom, LocalDate tom) {
        return new UttakplanPeriodeBuilder(UttaksplanPeriodeDtoOLD.UttakType.PERIODEUTENUTTAK, null, fom, tom);
    }

    private UttakplanPeriodeBuilder(UttaksplanPeriodeDtoOLD.UttakType type, StønadskontoType konto, LocalDate fom, LocalDate tom) {
        this.type = type;
        this.konto = konto == null ? tilKontoType(StønadskontoType.IKKE_SATT) : tilKontoType(konto);
        this.tidsperiode = new ÅpenPeriodeDtoOLD(fom, tom);
    }

    public UttakplanPeriodeBuilder medTidsperiode(ÅpenPeriodeDtoOLD tidsperiode) {
        this.tidsperiode = tidsperiode;
        return this;
    }

    public UttakplanPeriodeBuilder medKonto(StønadskontoType konto) {
        this.konto = tilKontoType(konto);
        return this;
    }

    public UttakplanPeriodeBuilder medMorsAktivitetIPerioden(String morsAktivitetIPerioden) {
        this.morsAktivitetIPerioden = morsAktivitetIPerioden;
        return this;
    }

    public UttakplanPeriodeBuilder medÅrsak(String årsak) {
        this.årsak = årsak;
        return this;
    }

    public UttakplanPeriodeBuilder medSamtidigUttakProsent(Double samtidigUttakProsent) {
        this.samtidigUttakProsent = samtidigUttakProsent;
        return this;
    }

    public UttakplanPeriodeBuilder medStillingsprosent(Double stillingsprosent) {
        this.stillingsprosent = stillingsprosent;
        return this;
    }

    public UttakplanPeriodeBuilder medErArbeidstaker(boolean erArbeidstaker) {
        this.erArbeidstaker = erArbeidstaker;
        return this;
    }

    public UttakplanPeriodeBuilder medErFrilanser(boolean erFrilanser) {
        this.erFrilanser = erFrilanser;
        return this;
    }

    public UttakplanPeriodeBuilder medErSelvstendig(boolean erSelvstendig) {
        this.erSelvstendig = erSelvstendig;
        return this;
    }

    public UttakplanPeriodeBuilder medGradert(boolean gradert) {
        this.gradert = gradert;
        return this;
    }

    public UttakplanPeriodeBuilder medØnskerFlerbarnsdager(boolean ønskerFlerbarnsdager) {
        this.ønskerFlerbarnsdager = ønskerFlerbarnsdager;
        return this;
    }

    public UttakplanPeriodeBuilder medØnskerSamtidigUttak(boolean ønskerSamtidigUttak) {
        this.ønskerSamtidigUttak = ønskerSamtidigUttak;
        return this;
    }

    public UttakplanPeriodeBuilder medJusteresVedFødsel(Boolean justeresVedFødsel) {
        this.justeresVedFødsel = justeresVedFødsel;
        return this;
    }

    public UttakplanPeriodeBuilder medOrgnumre(List<String> orgnumre) {
        this.orgnumre = orgnumre;
        return this;
    }

    public static UttaksplanPeriodeDtoOLD.KontoType tilKontoType(StønadskontoType konto) {
        return switch (konto) {
            case IKKE_SATT -> null;
            case FELLESPERIODE -> UttaksplanPeriodeDtoOLD.KontoType.FELLESPERIODE;
            case MØDREKVOTE -> UttaksplanPeriodeDtoOLD.KontoType.MØDREKVOTE;
            case FEDREKVOTE -> UttaksplanPeriodeDtoOLD.KontoType.FEDREKVOTE;
            case FORELDREPENGER -> UttaksplanPeriodeDtoOLD.KontoType.FORELDREPENGER;
            case FORELDREPENGER_FØR_FØDSEL -> UttaksplanPeriodeDtoOLD.KontoType.FORELDREPENGER_FØR_FØDSEL;
        };
    }

    public UttaksplanPeriodeDtoOLD build() {
        return new UttaksplanPeriodeDtoOLD(type,
            tidsperiode,
            konto,
            morsAktivitetIPerioden,
            årsak,
            samtidigUttakProsent,
            stillingsprosent,
            erArbeidstaker,
            erFrilanser,
            erSelvstendig,
            gradert,
            ønskerFlerbarnsdager,
            ønskerSamtidigUttak,
            justeresVedFødsel,
            orgnumre);
    }
}
