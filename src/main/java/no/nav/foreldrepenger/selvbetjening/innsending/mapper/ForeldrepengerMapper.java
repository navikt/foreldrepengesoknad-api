package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import no.nav.foreldrepenger.common.domain.Søker;
import no.nav.foreldrepenger.common.domain.Søknad;
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

import java.time.LocalDate;
import java.util.List;

import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilAnnenForelder;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilMedlemskap;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilOpptjening;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilRelasjonTilBarn;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilVedleggsreferanse;

final class ForeldrepengerMapper {

    private ForeldrepengerMapper() {
    }

    static no.nav.foreldrepenger.common.domain.Søknad tilForeldrepengesøknad(ForeldrepengesøknadFrontend foreldrepengesøknad) {
        if (Boolean.TRUE.equals(foreldrepengesøknad.getErEndringssøknad())) {
            return new Endringssøknad(
                LocalDate.now(),
                tilSøker(foreldrepengesøknad),
                tilYtelse(foreldrepengesøknad),
                foreldrepengesøknad.getTilleggsopplysninger(),
                tilVedlegg(foreldrepengesøknad.getVedlegg()),
                foreldrepengesøknad.getSaksnummer());
        }
        return new Søknad(
            LocalDate.now(),
            tilSøker(foreldrepengesøknad),
            tilYtelse(foreldrepengesøknad),
            foreldrepengesøknad.getTilleggsopplysninger(),
            tilVedlegg(foreldrepengesøknad.getVedlegg())
        );
    }

    private static Søker tilSøker(ForeldrepengesøknadFrontend f) {
        var søker = f.getSøker();
        if (søker == null) {
            throw new IllegalStateException("Kan ikke ha tom søkerobjekt");
        }
        return new Søker(søker.rolle(), søker.språkkode());
    }


    private static Foreldrepenger tilYtelse(ForeldrepengesøknadFrontend f) {
        return new Foreldrepenger(
            tilAnnenForelder(f),
            tilRelasjonTilBarn(f),
            tilRettigheter(f),
            Dekningsgrad.fraKode(f.getDekningsgrad()),
            tilOpptjening(f),
            tilFordeling(f),
            tilMedlemskap(f)
        );
    }

    private static Rettigheter tilRettigheter(ForeldrepengesøknadFrontend f) {
        return new Rettigheter(
            f.getAnnenForelder().harRettPåForeldrepenger(),
            f.getSøker().erAleneOmOmsorg(),
            f.getAnnenForelder().harMorUføretrygd(),
            f.getAnnenForelder().harAnnenForelderOppholdtSegIEØS(),
            f.getAnnenForelder().harAnnenForelderTilsvarendeRettEØS());
    }


    private static Fordeling tilFordeling(ForeldrepengesøknadFrontend f) {
        return new Fordeling(
            f.getAnnenForelder().erInformertOmSøknaden(),
            tilLukketPeriodeMedVedlegg(f.getUttaksplan()),
            f.isØnskerJustertUttakVedFødsel()
        );
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
        return new OverføringsPeriode(
            u.tidsperiode().fom(),
            u.tidsperiode().tom(),
            u.årsak() != null ? Overføringsårsak.valueOf(u.årsak()) : null,
            StønadskontoType.valueSafelyOf(u.konto()),
            tilVedleggsreferanse(u.vedlegg())
        );
    }

    private static FriUtsettelsesPeriode tilFriUtsettelsesPeriode(UttaksplanPeriode u) {
        return new FriUtsettelsesPeriode(
            u.tidsperiode().fom(),
            u.tidsperiode().tom(),
            u.erArbeidstaker(),
            UtsettelsesÅrsak.valueOf(u.årsak()),
            u.morsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.morsAktivitetIPerioden()) : null,
            tilVedleggsreferanse(u.vedlegg())
        );
    }

    private static UtsettelsesPeriode tilUtsettelsesPeriode(UttaksplanPeriode u) {
        return new UtsettelsesPeriode(
            u.tidsperiode().fom(),
            u.tidsperiode().tom(),
            u.erArbeidstaker(),
            UtsettelsesÅrsak.valueOf(u.årsak()),
            u.morsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.morsAktivitetIPerioden()) : null,
            tilVedleggsreferanse(u.vedlegg())
        );
    }

    private static OppholdsPeriode tilOppholdsPeriode(UttaksplanPeriode u) {
        return new OppholdsPeriode(
            u.tidsperiode().fom(),
            u.tidsperiode().tom(),
            u.årsak() != null ? Oppholdsårsak.valueOf(u.årsak()) : null,
            tilVedleggsreferanse(u.vedlegg())
        );
    }

    private static UttaksPeriode tilUttaksPeriode(UttaksplanPeriode u) {
        return new UttaksPeriode(
            u.tidsperiode().fom(),
            u.tidsperiode().tom(),
            tilVedleggsreferanse(u.vedlegg()),
            StønadskontoType.valueSafelyOf(u.konto()),
            u.ønskerSamtidigUttak(),
            u.morsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.morsAktivitetIPerioden()) : null,
            u.ønskerFlerbarnsdager(),
            u.samtidigUttakProsent() != null ? ProsentAndel.valueOf(u.samtidigUttakProsent()) : null,
            u.justeresVedFødsel()
        );
    }

    private static GradertUttaksPeriode tilGradertUttaksperiode(UttaksplanPeriode u) {
        return new GradertUttaksPeriode(
            u.tidsperiode().fom(),
            u.tidsperiode().tom(),
            tilVedleggsreferanse(u.vedlegg()),
            StønadskontoType.valueSafelyOf(u.konto()),
            u.ønskerSamtidigUttak(),
            u.morsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.morsAktivitetIPerioden()) : null,
            u.ønskerFlerbarnsdager(),
            u.samtidigUttakProsent() != null ? ProsentAndel.valueOf(u.samtidigUttakProsent()) : null,
            u.stillingsprosent() != null ? ProsentAndel.valueOf(u.stillingsprosent()) : null,
            u.erArbeidstaker(),
            u.orgnumre(),
            true,
            u.erFrilanser(),
            u.erSelvstendig(),
            u.justeresVedFødsel()
        );
    }
}
