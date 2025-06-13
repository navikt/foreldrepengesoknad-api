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
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.AnnenforelderDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.ForeldrepengesøknadDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.UttaksplanPeriodeDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;

final class ForeldrepengerMapper {

    private ForeldrepengerMapper() {
    }

    static Søknad tilForeldrepengesøknadVedleggUtenInnhold(ForeldrepengesøknadDtoOLD foreldrepengesøknad,
                                                           List<VedleggDto> påkrevdeVedlegg,
                                                           LocalDate mottattDato) {
        return new Søknad(mottattDato, tilSøker(foreldrepengesøknad.søker()), tilYtelse(foreldrepengesøknad, påkrevdeVedlegg),
            foreldrepengesøknad.tilleggsopplysninger(), tilVedlegg(påkrevdeVedlegg));
    }

    static Søker tilSøker(SøkerDtoOLD søker) {
        return new Søker(søker.rolle(), søker.språkkode());
    }

    private static Foreldrepenger tilYtelse(ForeldrepengesøknadDtoOLD f, List<VedleggDto> vedlegg) {
        return new Foreldrepenger(tilAnnenForelder(f.annenForelder()), tilRelasjonTilBarn(f.barn(), f.situasjon(), vedlegg),
            tilRettigheter(f.søker(), f.annenForelder()), Dekningsgrad.fraKode(f.dekningsgrad().verdi()), tilOpptjening(f.søker(), vedlegg),
            tilFordeling(f, vedlegg), tilOppholdIUtlandet(f));
    }

    static Rettigheter tilRettigheter(SøkerDtoOLD søker, AnnenforelderDtoOLD annenforelder) {
        return new Rettigheter(annenforelder.harRettPåForeldrepenger(), søker.erAleneOmOmsorg(), annenforelder.harMorUføretrygd(),
            annenforelder.harAnnenForelderOppholdtSegIEØS(), annenforelder.harAnnenForelderTilsvarendeRettEØS());
    }


    private static Fordeling tilFordeling(ForeldrepengesøknadDtoOLD f, List<VedleggDto> vedlegg) {
        return new Fordeling(f.annenForelder().erInformertOmSøknaden(), tilLukketPeriodeMedVedlegg(f.uttaksplan(), vedlegg),
            f.ønskerJustertUttakVedFødsel());
    }

    static List<LukketPeriodeMedVedlegg> tilLukketPeriodeMedVedlegg(List<UttaksplanPeriodeDtoOLD> uttaksplan, List<VedleggDto> vedlegg) {
        return uttaksplan.stream().map(u -> tilLukketPeriodeMedVedlegg(u, vedlegg)).toList();
    }

    private static LukketPeriodeMedVedlegg tilLukketPeriodeMedVedlegg(UttaksplanPeriodeDtoOLD u, List<VedleggDto> vedlegg) {
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

    private static OverføringsPeriode tilOverføringsPeriode(UttaksplanPeriodeDtoOLD u, List<VedleggDto> vedlegg) {
        return new OverføringsPeriode(u.tidsperiode().fom(), u.tidsperiode().tom(), u.årsak() != null ? Overføringsårsak.valueOf(u.årsak()) : null,
            tilStønadskontoType(u.konto()),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererUttaksperiode(vedlegg, u.tidsperiode())));
    }

    private static FriUtsettelsesPeriode tilFriUtsettelsesPeriode(UttaksplanPeriodeDtoOLD u, List<VedleggDto> vedlegg) {
        return new FriUtsettelsesPeriode(u.tidsperiode().fom(), u.tidsperiode().tom(), u.erArbeidstaker(), UtsettelsesÅrsak.valueOf(u.årsak()),
            u.morsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.morsAktivitetIPerioden()) : null,
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererUttaksperiode(vedlegg, u.tidsperiode())));
    }

    private static UtsettelsesPeriode tilUtsettelsesPeriode(UttaksplanPeriodeDtoOLD u, List<VedleggDto> vedlegg) {
        return new UtsettelsesPeriode(u.tidsperiode().fom(), u.tidsperiode().tom(), u.erArbeidstaker(), UtsettelsesÅrsak.valueOf(u.årsak()),
            u.morsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.morsAktivitetIPerioden()) : null,
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererUttaksperiode(vedlegg, u.tidsperiode())));
    }

    private static OppholdsPeriode tilOppholdsPeriode(UttaksplanPeriodeDtoOLD u, List<VedleggDto> vedlegg) {
        return new OppholdsPeriode(u.tidsperiode().fom(), u.tidsperiode().tom(), u.årsak() != null ? Oppholdsårsak.valueOf(u.årsak()) : null,
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererUttaksperiode(vedlegg, u.tidsperiode())));
    }

    private static UttaksPeriode tilUttaksPeriode(UttaksplanPeriodeDtoOLD u, List<VedleggDto> vedlegg) {
        return new UttaksPeriode(u.tidsperiode().fom(), u.tidsperiode().tom(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererUttaksperiode(vedlegg, u.tidsperiode())),
            tilStønadskontoType(u.konto()), u.ønskerSamtidigUttak(),
            u.morsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.morsAktivitetIPerioden()) : null, u.ønskerFlerbarnsdager(),
            u.samtidigUttakProsent() != null ? ProsentAndel.valueOf(u.samtidigUttakProsent()) : null, u.justeresVedFødsel());
    }

    private static GradertUttaksPeriode tilGradertUttaksperiode(UttaksplanPeriodeDtoOLD u, List<VedleggDto> vedlegg) {
        return new GradertUttaksPeriode(u.tidsperiode().fom(), u.tidsperiode().tom(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererUttaksperiode(vedlegg, u.tidsperiode())),
            tilStønadskontoType(u.konto()), u.ønskerSamtidigUttak(),
            u.morsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.morsAktivitetIPerioden()) : null, u.ønskerFlerbarnsdager(),
            u.samtidigUttakProsent() != null ? ProsentAndel.valueOf(u.samtidigUttakProsent()) : null,
            u.stillingsprosent() != null ? ProsentAndel.valueOf(u.stillingsprosent()) : null, u.erArbeidstaker(), u.orgnumre(), true, u.erFrilanser(),
            u.erSelvstendig(), u.justeresVedFødsel());
    }

    public static StønadskontoType tilStønadskontoType(UttaksplanPeriodeDtoOLD.KontoType konto) {
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


    static AnnenForelder tilAnnenForelder(AnnenforelderDtoOLD annenForelder) {
        if (annenForelder == null) {
            return new UkjentForelder();
        }
        return switch (annenForelder.type()) {
            case NORSK -> tilNorskForelder(annenForelder);
            case UTENLANDSK -> tilUtenlandskForelder(annenForelder);
            case IKKE_OPPGITT -> new UkjentForelder();
        };
    }

    private static UtenlandskForelder tilUtenlandskForelder(AnnenforelderDtoOLD annenForelder) {
        return new UtenlandskForelder(annenForelder.fnr(), CommonMapper.land(annenForelder.bostedsland()), navn(annenForelder));
    }

    private static NorskForelder tilNorskForelder(AnnenforelderDtoOLD annenForelder) {
        return new NorskForelder(new Fødselsnummer(annenForelder.fnr()), navn(annenForelder));
    }

    private static String navn(AnnenforelderDtoOLD annenForelder) {
        return annenForelder.fornavn() + " " + annenForelder.etternavn();
    }
}
