package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.Oppholdsårsak;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.Overføringsårsak;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UtsettelsesÅrsak;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.UttaksplanPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ÅpenPeriodeDto;

public class UttakplanPeriodeBuilder {
    UttaksplanPeriodeDto.Type type;
    ÅpenPeriodeDto tidsperiode;
    String forelder;
    UttaksplanPeriodeDto.KontoType konto;
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
    List<MutableVedleggReferanseDto> vedlegg;

    public static UttakplanPeriodeBuilder uttak(StønadskontoType konto, LocalDate fom, LocalDate tom) {
        return new UttakplanPeriodeBuilder(UttaksplanPeriodeDto.Type.UTTAK, konto, fom, tom);
    }

    public static UttakplanPeriodeBuilder gradert(StønadskontoType konto, LocalDate fom, LocalDate tom, Double stillingsprosent) {
        return new UttakplanPeriodeBuilder(UttaksplanPeriodeDto.Type.UTTAK, konto, fom, tom)
            .medStillingsprosent(stillingsprosent)
            .medGradert(true);
    }

    public static UttakplanPeriodeBuilder opphold(Oppholdsårsak oppholdsårsak, LocalDate fom, LocalDate tom) {
        return new UttakplanPeriodeBuilder(UttaksplanPeriodeDto.Type.OPPHOLD, null, fom, tom)
            .medÅrsak(oppholdsårsak.name());
    }

    public static UttakplanPeriodeBuilder overføring(Overføringsårsak årsak, StønadskontoType konto, LocalDate fom, LocalDate tom) {
        return new UttakplanPeriodeBuilder(UttaksplanPeriodeDto.Type.OVERFØRING, konto, fom, tom)
            .medÅrsak(årsak.name());
    }

    public static UttakplanPeriodeBuilder utsettelse(UtsettelsesÅrsak årsak, LocalDate fom, LocalDate tom) {
        return new UttakplanPeriodeBuilder(UttaksplanPeriodeDto.Type.UTSETTELSE, null, fom, tom)
            .medÅrsak(årsak.name());
    }

    public static UttakplanPeriodeBuilder friUtsettelse(LocalDate fom, LocalDate tom) {
        return new UttakplanPeriodeBuilder(UttaksplanPeriodeDto.Type.PERIODEUTENUTTAK, null, fom, tom);
    }

    private UttakplanPeriodeBuilder(UttaksplanPeriodeDto.Type type, StønadskontoType konto, LocalDate fom, LocalDate tom) {
        this.type = type;
        this.konto = tilKontoType(konto);
        this.tidsperiode = new ÅpenPeriodeDto(fom, tom);
    }

    public UttakplanPeriodeBuilder medTidsperiode(ÅpenPeriodeDto tidsperiode) {
        this.tidsperiode = tidsperiode;
        return this;
    }

    public UttakplanPeriodeBuilder medForelder(String forelder) {
        this.forelder = forelder;
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

    public UttakplanPeriodeBuilder medVedlegg(List<MutableVedleggReferanseDto> vedlegg) {
        this.vedlegg = vedlegg;
        return this;
    }

    public static UttaksplanPeriodeDto.KontoType tilKontoType(StønadskontoType konto) {
        return switch (konto) {
            case IKKE_SATT -> null;
            case FELLESPERIODE -> UttaksplanPeriodeDto.KontoType.FELLESPERIODE;
            case MØDREKVOTE -> UttaksplanPeriodeDto.KontoType.MØDREKVOTE;
            case FEDREKVOTE -> UttaksplanPeriodeDto.KontoType.FEDREKVOTE;
            case FORELDREPENGER -> UttaksplanPeriodeDto.KontoType.FORELDREPENGER;
            case FORELDREPENGER_FØR_FØDSEL -> UttaksplanPeriodeDto.KontoType.FORELDREPENGER_FØR_FØDSEL;
        };
    }

    public UttaksplanPeriodeDto build() {
        return new UttaksplanPeriodeDto(
            type,
            tidsperiode,
            forelder,
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
            orgnumre,
            vedlegg
        );
    }
}
