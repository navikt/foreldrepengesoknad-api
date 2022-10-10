package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilAnnenForelder;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilMedlemskap;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilOpptjening;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilRelasjonTilBarn;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.common.domain.Søker;
import no.nav.foreldrepenger.common.domain.felles.ProsentAndel;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Dekningsgrad;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Endringssøknad;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Foreldrepenger;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Rettigheter;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.Fordeling;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.FriUtsettelsesPeriode;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.GradertUttaksPeriode;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.LukketPeriodeMedVedlegg;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.MorsAktivitet;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.OppholdsPeriode;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.Oppholdsårsak;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.OverføringsPeriode;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.Overføringsårsak;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UtsettelsesPeriode;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UtsettelsesÅrsak;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UttaksPeriode;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.ForeldrepengesøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.UttaksplanPeriode;

final class ForeldrepengerMapper {

    private ForeldrepengerMapper() {
    }

    static no.nav.foreldrepenger.common.domain.Søknad tilForeldrepengesøknad(ForeldrepengesøknadFrontend foreldrepengesøknad) {
        if (Boolean.TRUE.equals(foreldrepengesøknad.getErEndringssøknad())) {
            return new Endringssøknad(
                foreldrepengesøknad.getSaksnummer(),
                null, // Settes senere
                tilSøker(foreldrepengesøknad),
                tilYtelse(foreldrepengesøknad),
                new ArrayList<>()); // Settes av InnsendingConnection etter logging
        }
        return no.nav.foreldrepenger.common.domain.Søknad.builder()
            .søker(tilSøker(foreldrepengesøknad))
            .ytelse(tilYtelse(foreldrepengesøknad))
            .vedlegg(new ArrayList<>()) // Settes av InnsendingConnection etter logging
            .build();
    }

    private static Søker tilSøker(ForeldrepengesøknadFrontend f) {
        var søker = f.getSøker();
        if (søker == null) {
            throw new IllegalStateException("Kan ikke ha tom søkerobjekt");
        }
        return new Søker(søker.rolle(), søker.språkkode());
    }


    private static Foreldrepenger tilYtelse(ForeldrepengesøknadFrontend f) {
        var foreldrepengerBuilder = Foreldrepenger.builder();
        if (Boolean.FALSE.equals(f.getErEndringssøknad())) {
            foreldrepengerBuilder
                .medlemsskap(tilMedlemskap(f))
                .opptjening(tilOpptjening(f));
        }
        return foreldrepengerBuilder
            .annenForelder(tilAnnenForelder(f))
            .relasjonTilBarn(tilRelasjonTilBarn(f))
            .dekningsgrad(Dekningsgrad.fraKode(f.getDekningsgrad()))
            .fordeling(tilFordeling(f))
            .rettigheter(tilRettigheter(f))
            .build();
    }

    private static Rettigheter tilRettigheter(ForeldrepengesøknadFrontend f) {
        return new Rettigheter(
            f.getAnnenForelder().harRettPåForeldrepenger(),
            f.getSøker().erAleneOmOmsorg(),
            f.getAnnenForelder().harMorUføretrygd(),
            f.getAnnenForelder().harAnnenForelderTilsvarendeRettEØS());
    }


    private static Fordeling tilFordeling(ForeldrepengesøknadFrontend f) {
        return Fordeling.builder()
            .perioder(tilLukketPeriodeMedVedlegg(f.getUttaksplan()))
            .ønskerJustertUttakVedFødsel(f.isØnskerJustertUttakVedFødsel())
            .erAnnenForelderInformert(f.getAnnenForelder().erInformertOmSøknaden())
            .build();
    }

    private static List<LukketPeriodeMedVedlegg> tilLukketPeriodeMedVedlegg(List<UttaksplanPeriode> uttaksplan) {
        return uttaksplan.stream()
            .map(ForeldrepengerMapper::tilLukketPeriodeMedVedlegg)
            .toList();
    }

    private static LukketPeriodeMedVedlegg tilLukketPeriodeMedVedlegg(UttaksplanPeriode u) {
        if (u.gradert() || u.type().equalsIgnoreCase("gradert")) {
            return tilGradertUttaksperiode(u);
        }
        return switch (u.type()) {
            case "uttak" -> tilUttaksPeriode(u);
            case "opphold" -> tilOppholdsPeriode(u);
            case "periodeUtenUttak" -> tilFriUtsettelsesPeriode(u); // periodeUtenUttak er ekvivalent med "fri" i mottak
            case "utsettelse" -> tilUtsettelsesPeriode(u);
            case "overføring" -> tilOverføringsPeriode(u);
            default -> throw new IllegalStateException("Ikke støttet periodetype: " + u.type());
        };
    }

    private static OverføringsPeriode tilOverføringsPeriode(UttaksplanPeriode u) {
        return OverføringsPeriode.builder()
            .årsak(u.årsak() != null ? Overføringsårsak.valueOf(u.årsak()) : null)
            .uttaksperiodeType(StønadskontoType.valueSafelyOf(u.konto()))
            .fom(u.tidsperiode().fom())
            .tom(u.tidsperiode().tom())
            .vedlegg(u.vedlegg())
            .build();
    }

    private static FriUtsettelsesPeriode tilFriUtsettelsesPeriode(UttaksplanPeriode u) {
        return FriUtsettelsesPeriode.FriUtsettelsesPeriodeBuilder()
            .årsak(UtsettelsesÅrsak.valueOf(u.årsak()))
            .erArbeidstaker(u.erArbeidstaker())
            .morsAktivitetsType(u.morsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.morsAktivitetIPerioden()) : null)
            .fom(u.tidsperiode().fom())
            .tom(u.tidsperiode().tom())
            .vedlegg(u.vedlegg())
            .build();

    }

    private static UtsettelsesPeriode tilUtsettelsesPeriode(UttaksplanPeriode u) {
        return UtsettelsesPeriode.UtsettelsesPeriodeBuilder()
            .årsak(UtsettelsesÅrsak.valueOf(u.årsak()))
            .erArbeidstaker(u.erArbeidstaker())
            .virksomhetsnummer(u.orgnumre())
            .morsAktivitetsType(u.morsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.morsAktivitetIPerioden()) : null)
            .fom(u.tidsperiode().fom())
            .tom(u.tidsperiode().tom())
            .vedlegg(u.vedlegg())
            .build();
    }

    private static OppholdsPeriode tilOppholdsPeriode(UttaksplanPeriode u) {
        return OppholdsPeriode.builder()
            .årsak(u.årsak() != null ? Oppholdsårsak.valueOf(u.årsak()) : null)
            .fom(u.tidsperiode().fom())
            .tom(u.tidsperiode().tom())
            .vedlegg(u.vedlegg())
            .build();
    }

    private static UttaksPeriode tilUttaksPeriode(UttaksplanPeriode u) {
        return UttaksPeriode.UttaksPeriodeBuilder()
            .uttaksperiodeType(StønadskontoType.valueSafelyOf(u.konto()))
            .ønskerSamtidigUttak(u.ønskerSamtidigUttak())
            .morsAktivitetsType(u.morsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.morsAktivitetIPerioden()) : null)
            .ønskerFlerbarnsdager(u.ønskerFlerbarnsdager())
            .samtidigUttakProsent(u.samtidigUttakProsent() != null ? ProsentAndel.valueOf(u.samtidigUttakProsent()) : null)
            .justeresVedFødsel(u.justeresVedFødsel())
            .fom(u.tidsperiode().fom())
            .tom(u.tidsperiode().tom())
            .vedlegg(u.vedlegg())
            .build();
    }

    private static GradertUttaksPeriode tilGradertUttaksperiode(UttaksplanPeriode u) {
        return GradertUttaksPeriode.GradertUttaksPeriodeBuilder()
            .arbeidsForholdSomskalGraderes(true)
            .arbeidstidProsent(u.stillingsprosent() != null ? ProsentAndel.valueOf(u.stillingsprosent()) : null)
            .erArbeidstaker(u.erArbeidstaker())
            .justeresVedFødsel(u.justeresVedFødsel())
            .virksomhetsnummer(u.orgnumre())
            .frilans(u.erFrilanser())
            .selvstendig(u.erSelvstendig())

            .uttaksperiodeType(StønadskontoType.valueSafelyOf(u.konto()))
            .ønskerSamtidigUttak(u.ønskerSamtidigUttak())
            .morsAktivitetsType(u.morsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.morsAktivitetIPerioden()) : null)
            .ønskerFlerbarnsdager(u.ønskerFlerbarnsdager())
            .samtidigUttakProsent(u.samtidigUttakProsent() != null ? ProsentAndel.valueOf(u.samtidigUttakProsent()) : null)

            .fom(u.tidsperiode().fom())
            .tom(u.tidsperiode().tom())
            .vedlegg(u.vedlegg())
            .build();
    }
}
