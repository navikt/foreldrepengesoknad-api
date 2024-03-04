package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FEDREKVOTE;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FELLESPERIODE;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FORELDREPENGER;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FORELDREPENGER_FØR_FØDSEL;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.MØDREKVOTE;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilOppholdIUtlandet;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilOpptjening;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilRelasjonTilBarn;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedleggsreferanse;

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
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UtsettelsesPeriode;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UtsettelsesÅrsak;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UttaksPeriode;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.AnnenforelderDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.UttaksplanPeriodeDto;

final class ForeldrepengerMapper {

    private ForeldrepengerMapper() {
    }

    static Søknad tilForeldrepengesøknadVedleggUtenInnhold(ForeldrepengesøknadDto foreldrepengesøknad, LocalDate mottattDato) {
        var vedlegg = foreldrepengesøknad.vedlegg();
        return new Søknad(
            mottattDato,
            tilSøker(foreldrepengesøknad.søker()),
            tilYtelse(foreldrepengesøknad, vedlegg),
            foreldrepengesøknad.tilleggsopplysninger(),
            tilVedlegg(vedlegg)
        );
    }

    static Søker tilSøker(SøkerDto søker) {
        return new Søker(søker.rolle(), søker.språkkode());
    }

    private static Foreldrepenger tilYtelse(ForeldrepengesøknadDto f, List<VedleggDto> vedlegg) {
        return new Foreldrepenger(
            tilAnnenForelder(f.annenForelder()),
            tilRelasjonTilBarn(f.barn(), f.situasjon(), vedlegg),
            tilRettigheter(f.søker(), f.annenForelder()),
            Dekningsgrad.fraKode(f.dekningsgrad().verdi()),
            tilOpptjening(f.søker(), vedlegg),
            tilFordeling(f, vedlegg),
            tilOppholdIUtlandet(f)
        );
    }

    static Rettigheter tilRettigheter(SøkerDto søker, AnnenforelderDto annenforelder) {
        return new Rettigheter(
            annenforelder.harRettPåForeldrepenger(),
            søker.erAleneOmOmsorg(),
            annenforelder.harMorUføretrygd(),
            annenforelder.harAnnenForelderOppholdtSegIEØS(),
            annenforelder.harAnnenForelderTilsvarendeRettEØS());
    }


    private static Fordeling tilFordeling(ForeldrepengesøknadDto f, List<VedleggDto> vedlegg) {
        return new Fordeling(
            f.annenForelder().erInformertOmSøknaden(),
            tilLukketPeriodeMedVedlegg(f.uttaksplan(), vedlegg),
            f.ønskerJustertUttakVedFødsel()
        );
    }

    static List<LukketPeriodeMedVedlegg> tilLukketPeriodeMedVedlegg(List<UttaksplanPeriodeDto> uttaksplan, List<VedleggDto> vedlegg) {
        return uttaksplan.stream()
            .map(u -> tilLukketPeriodeMedVedlegg(u, vedlegg))
            .toList();
    }

    private static LukketPeriodeMedVedlegg tilLukketPeriodeMedVedlegg(UttaksplanPeriodeDto u, List<VedleggDto> vedlegg) {
        if (u.gradert()) {
            return tilGradertUttaksperiode(u, vedlegg);
        }
        return switch (u.type()) {
            case UTTAK -> tilUttaksPeriode(u, vedlegg);
            case OPPHOLD -> tilOppholdsPeriode(u, vedlegg);
            case PERIODEUTENUTTAK -> tilFriUtsettelsesPeriode(u, vedlegg); // periodeUtenUttak er ekvivalent med "fri" i mottak
            case UTSETTELSE -> tilUtsettelsesPeriode(u, vedlegg);
            case OVERFØRING -> tilOverføringsPeriode(u, vedlegg);
        };
    }

    private static OverføringsPeriode tilOverføringsPeriode(UttaksplanPeriodeDto u, List<VedleggDto> vedlegg) {
        return new OverføringsPeriode(
            u.tidsperiode().fom(),
            u.tidsperiode().tom(),
            u.årsak() != null ? Overføringsårsak.valueOf(u.årsak()) : null,
            tilStønadskontoType(u.konto()),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererUttaksperiode(vedlegg, u.tidsperiode()))
        );
    }

    private static FriUtsettelsesPeriode tilFriUtsettelsesPeriode(UttaksplanPeriodeDto u, List<VedleggDto> vedlegg) {
        return new FriUtsettelsesPeriode(
            u.tidsperiode().fom(),
            u.tidsperiode().tom(),
            u.erArbeidstaker(),
            UtsettelsesÅrsak.valueOf(u.årsak()),
            u.morsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.morsAktivitetIPerioden()) : null,
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererUttaksperiode(vedlegg, u.tidsperiode()))
        );
    }

    private static UtsettelsesPeriode tilUtsettelsesPeriode(UttaksplanPeriodeDto u, List<VedleggDto> vedlegg) {
        return new UtsettelsesPeriode(
            u.tidsperiode().fom(),
            u.tidsperiode().tom(),
            u.erArbeidstaker(),
            UtsettelsesÅrsak.valueOf(u.årsak()),
            u.morsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.morsAktivitetIPerioden()) : null,
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererUttaksperiode(vedlegg, u.tidsperiode()))
        );
    }

    private static OppholdsPeriode tilOppholdsPeriode(UttaksplanPeriodeDto u, List<VedleggDto> vedlegg) {
        return new OppholdsPeriode(
            u.tidsperiode().fom(),
            u.tidsperiode().tom(),
            u.årsak() != null ? Oppholdsårsak.valueOf(u.årsak()) : null,
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererUttaksperiode(vedlegg, u.tidsperiode()))
        );
    }

    private static UttaksPeriode tilUttaksPeriode(UttaksplanPeriodeDto u, List<VedleggDto> vedlegg) {
        return new UttaksPeriode(
            u.tidsperiode().fom(),
            u.tidsperiode().tom(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererUttaksperiode(vedlegg, u.tidsperiode())),
            tilStønadskontoType(u.konto()),
            u.ønskerSamtidigUttak(),
            u.morsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.morsAktivitetIPerioden()) : null,
            u.ønskerFlerbarnsdager(),
            u.samtidigUttakProsent() != null ? ProsentAndel.valueOf(u.samtidigUttakProsent()) : null,
            u.justeresVedFødsel()
        );
    }

    private static GradertUttaksPeriode tilGradertUttaksperiode(UttaksplanPeriodeDto u, List<VedleggDto> vedlegg) {
        return new GradertUttaksPeriode(
            u.tidsperiode().fom(),
            u.tidsperiode().tom(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererUttaksperiode(vedlegg, u.tidsperiode())),
            tilStønadskontoType(u.konto()),
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

    public static StønadskontoType tilStønadskontoType(UttaksplanPeriodeDto.KontoType konto) {
        if (konto == null) {
            return StønadskontoType.IKKE_SATT;
        }
        return switch (konto) {
            case FELLESPERIODE -> FELLESPERIODE;
            case MØDREKVOTE -> MØDREKVOTE;
            case FEDREKVOTE -> FEDREKVOTE;
            case FORELDREPENGER -> FORELDREPENGER;
            case FORELDREPENGER_FØR_FØDSEL -> FORELDREPENGER_FØR_FØDSEL;
        };
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
        return new NorskForelder(
            new Fødselsnummer(annenForelder.fnr()),
            navn(annenForelder));
    }

    private static String navn(AnnenforelderDto annenForelder) {
        return annenForelder.fornavn() + " " + annenForelder.etternavn();
    }
}
