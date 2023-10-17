package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilMedlemskap;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilOpptjening;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilRelasjonTilBarn;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilVedleggsreferanse;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Søker;
import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.common.domain.felles.ProsentAndel;
import no.nav.foreldrepenger.common.domain.felles.annenforelder.AnnenForelder;
import no.nav.foreldrepenger.common.domain.felles.annenforelder.NorskForelder;
import no.nav.foreldrepenger.common.domain.felles.annenforelder.UkjentForelder;
import no.nav.foreldrepenger.common.domain.felles.annenforelder.UtenlandskForelder;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Dekningsgrad;
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
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UtsettelsesPeriode;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UtsettelsesÅrsak;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UttaksPeriode;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.foreldrepenger.AnnenforelderDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.foreldrepenger.UttaksplanPeriodeDto;

final class ForeldrepengerMapper {

    private ForeldrepengerMapper() {
    }

    static no.nav.foreldrepenger.common.domain.Søknad tilForeldrepengesøknad(ForeldrepengesøknadDto foreldrepengesøknad, LocalDate mottattDato) {
        return new Søknad(
            mottattDato,
            tilSøker(foreldrepengesøknad.søker()),
            tilYtelse(foreldrepengesøknad),
            foreldrepengesøknad.tilleggsopplysninger(),
            tilVedlegg(foreldrepengesøknad.vedlegg())
        );
    }

    static Søker tilSøker(SøkerDto søker) {
        return new Søker(søker.rolle(), søker.språkkode());
    }

    private static Foreldrepenger tilYtelse(ForeldrepengesøknadDto f) {
        return new Foreldrepenger(
            tilAnnenForelder(f.annenForelder()),
            tilRelasjonTilBarn(f.barn(), f.situasjon()),
            tilRettigheter(f),
            Dekningsgrad.fraKode(f.dekningsgrad().verdi()),
            tilOpptjening(f),
            tilFordeling(f),
            tilMedlemskap(f)
        );
    }

    private static Rettigheter tilRettigheter(ForeldrepengesøknadDto f) {
        var annenforelder = f.annenForelder();
        var søker = f.søker();
        return new Rettigheter(
            annenforelder.harRettPåForeldrepenger(),
            søker.erAleneOmOmsorg(),
            annenforelder.harMorUføretrygd(),
            annenforelder.harAnnenForelderOppholdtSegIEØS(),
            annenforelder.harAnnenForelderTilsvarendeRettEØS());
    }


    private static Fordeling tilFordeling(ForeldrepengesøknadDto f) {
        return new Fordeling(
            f.annenForelder().erInformertOmSøknaden(),
            tilLukketPeriodeMedVedlegg(f.uttaksplan()),
            f.ønskerJustertUttakVedFødsel()
        );
    }

    static List<LukketPeriodeMedVedlegg> tilLukketPeriodeMedVedlegg(List<UttaksplanPeriodeDto> uttaksplan) {
        return uttaksplan.stream()
            .map(ForeldrepengerMapper::tilLukketPeriodeMedVedlegg)
            .toList();
    }

    private static LukketPeriodeMedVedlegg tilLukketPeriodeMedVedlegg(UttaksplanPeriodeDto u) {
        if (u.gradert()) {
            return tilGradertUttaksperiode(u);
        }
        return switch (u.type()) {
            case UTTAK -> tilUttaksPeriode(u);
            case OPPHOLD -> tilOppholdsPeriode(u);
            case PERIODE_UTEN_UTTAK -> tilFriUtsettelsesPeriode(u); // periodeUtenUttak er ekvivalent med "fri" i mottak
            case UTSETTELSE -> tilUtsettelsesPeriode(u);
            case OVERFØRING -> tilOverføringsPeriode(u);
        };
    }

    private static OverføringsPeriode tilOverføringsPeriode(UttaksplanPeriodeDto u) {
        return new OverføringsPeriode(
            u.tidsperiode().fom(),
            u.tidsperiode().tom(),
            u.årsak() != null ? Overføringsårsak.valueOf(u.årsak()) : null,
            u.konto(),
            tilVedleggsreferanse(u.vedlegg())
        );
    }

    private static FriUtsettelsesPeriode tilFriUtsettelsesPeriode(UttaksplanPeriodeDto u) {
        return new FriUtsettelsesPeriode(
            u.tidsperiode().fom(),
            u.tidsperiode().tom(),
            u.erArbeidstaker(),
            UtsettelsesÅrsak.valueOf(u.årsak()),
            u.morsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.morsAktivitetIPerioden()) : null,
            tilVedleggsreferanse(u.vedlegg())
        );
    }

    private static UtsettelsesPeriode tilUtsettelsesPeriode(UttaksplanPeriodeDto u) {
        return new UtsettelsesPeriode(
            u.tidsperiode().fom(),
            u.tidsperiode().tom(),
            u.erArbeidstaker(),
            UtsettelsesÅrsak.valueOf(u.årsak()),
            u.morsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.morsAktivitetIPerioden()) : null,
            tilVedleggsreferanse(u.vedlegg())
        );
    }

    private static OppholdsPeriode tilOppholdsPeriode(UttaksplanPeriodeDto u) {
        return new OppholdsPeriode(
            u.tidsperiode().fom(),
            u.tidsperiode().tom(),
            u.årsak() != null ? Oppholdsårsak.valueOf(u.årsak()) : null,
            tilVedleggsreferanse(u.vedlegg())
        );
    }

    private static UttaksPeriode tilUttaksPeriode(UttaksplanPeriodeDto u) {
        return new UttaksPeriode(
            u.tidsperiode().fom(),
            u.tidsperiode().tom(),
            tilVedleggsreferanse(u.vedlegg()),
            u.konto(),
            u.ønskerSamtidigUttak(),
            u.morsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.morsAktivitetIPerioden()) : null,
            u.ønskerFlerbarnsdager(),
            u.samtidigUttakProsent() != null ? ProsentAndel.valueOf(u.samtidigUttakProsent()) : null,
            u.justeresVedFødsel()
        );
    }

    private static GradertUttaksPeriode tilGradertUttaksperiode(UttaksplanPeriodeDto u) {
        return new GradertUttaksPeriode(
            u.tidsperiode().fom(),
            u.tidsperiode().tom(),
            tilVedleggsreferanse(u.vedlegg()),
            u.konto(),
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

    static AnnenForelder tilAnnenForelder(AnnenforelderDto annenForelder) {
        if (annenForelder == null) {
            return new UkjentForelder();
        }
        return switch (annenForelder.type()) {
            case NORSK -> tilNorskForelder(annenForelder);
            case UTENLANDSK -> tilUtenlandskForelder(annenForelder);
            case IKKE_OPPGITT -> new UkjentForelder();
        };
    }

    private static UtenlandskForelder tilUtenlandskForelder(AnnenforelderDto annenForelder) {
        return new UtenlandskForelder(
            annenForelder.fnr(),
            CommonMapper.land(annenForelder.bostedsland()),
            navn(annenForelder)
        );
    }

    private static NorskForelder tilNorskForelder(AnnenforelderDto annenForelder) {
        return new NorskForelder(new Fødselsnummer(annenForelder.fnr()), navn(annenForelder));
    }

    private static String navn(AnnenforelderDto annenForelder) {
        return annenForelder.fornavn() + " " + annenForelder.etternavn();
    }
}
