package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper;

import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FEDREKVOTE;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FELLESPERIODE;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FORELDREPENGER;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FORELDREPENGER_FØR_FØDSEL;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.MØDREKVOTE;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedleggsreferanse;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.CommonMapper.tilOppholdIUtlandet;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.CommonMapper.tilOpptjening;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.CommonMapper.tilRelasjonTilBarn;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
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
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.OppholdsPeriode;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.OverføringsPeriode;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UtsettelsesPeriode;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UttaksPeriode;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ÅpenPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.DokumentasjonReferanseMapper;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.annenpart.AnnenForelderDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.annenpart.NorskForelderDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.annenpart.UtenlandskForelderDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.GradertUttaksPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.KontoType;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.OppholdsPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.OverføringsPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.UtsettelsesPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.UttaksPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.UttaksplanDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.Uttaksplanperiode;


public final class ForeldrepengerMapper {

    private ForeldrepengerMapper() {
    }

    public static Søknad tilForeldrepengesøknad(ForeldrepengesøknadDto foreldrepengesøknad, LocalDate mottattDato) {
        var vedlegg = foreldrepengesøknad.vedlegg();
        return new Søknad(
            mottattDato,
            tilSøker(foreldrepengesøknad.rolle(), foreldrepengesøknad.språkkode()),
            tilYtelse(foreldrepengesøknad, vedlegg),
            foreldrepengesøknad.tilleggsopplysninger(),
            tilVedlegg(vedlegg)
        );
    }

    static Søker tilSøker(BrukerRolle søknadsRolle, Målform målform) {
        return new Søker(søknadsRolle, målform);
    }

    private static Foreldrepenger tilYtelse(ForeldrepengesøknadDto f, List<VedleggDto> vedlegg) {
        return new Foreldrepenger(
            tilAnnenForelder(f.annenForelder()),
            tilRelasjonTilBarn(f.barn(), vedlegg),
            tilRettigheter(f.annenForelder()),
            Dekningsgrad.fraKode(f.dekningsgrad().verdi()),
            tilOpptjening(f.selvstendigNæringsdrivendeInformasjon(), f.frilansInformasjon(), f.andreInntekterSiste10Mnd(), vedlegg),
            tilUttaksplan(f.uttaksplan(), f.annenForelder(), vedlegg),
            tilOppholdIUtlandet(f.utenlandsopphold())
        );
    }

    static Rettigheter tilRettigheter(AnnenForelderDto annenforelder) {
        if (annenforelder == null) {
            return new Rettigheter(
                false,
                null,
                null,
                null,
                null
            );
        }
        var annenpartsRettigheter = annenforelder.rettigheter();
        return new Rettigheter(
            annenpartsRettigheter.harRettPåForeldrepenger(),
            annenpartsRettigheter.erAleneOmOmsorg(),
            annenpartsRettigheter.harMorUføretrygd(),
            annenpartsRettigheter.harAnnenForelderOppholdtSegIEØS(),
            annenpartsRettigheter.harAnnenForelderTilsvarendeRettEØS());
    }


    static Fordeling tilUttaksplan(UttaksplanDto uttaksplan, AnnenForelderDto annenforelder, List<VedleggDto> vedlegg) {
        return new Fordeling(
            erAnnenpartInformert(annenforelder),
            tilLukketPeriodeMedVedlegg(uttaksplan.uttaksperioder(), vedlegg),
            uttaksplan.ønskerJustertUttakVedFødsel()
        );
    }

    private static Boolean erAnnenpartInformert(AnnenForelderDto annenForelderDto) {
        if (annenForelderDto == null) {
            return false;
        }
        return annenForelderDto.rettigheter().erInformertOmSøknaden();
    }

    static List<LukketPeriodeMedVedlegg> tilLukketPeriodeMedVedlegg(List<Uttaksplanperiode> uttaksplan, List<VedleggDto> vedlegg) {
        return uttaksplan.stream()
            .map(u -> tilLukketPeriodeMedVedlegg(u, vedlegg))
            .toList();
    }

    private static LukketPeriodeMedVedlegg tilLukketPeriodeMedVedlegg(Uttaksplanperiode u, List<VedleggDto> vedlegg) {
        if (u instanceof UttaksPeriodeDto uttak) {
            return tilUttaksPeriode(uttak, vedlegg);
        }
        if (u instanceof GradertUttaksPeriodeDto gradertUttak) {
            return tilGradertUttaksperiode(gradertUttak, vedlegg);
        }
        if (u instanceof OverføringsPeriodeDto overføring) {
            return tilOverføringsPeriode(overføring, vedlegg);
        }
        if (u instanceof OppholdsPeriodeDto opphold) {
            return tilOppholdsPeriode(opphold, vedlegg);
        }
        if (u instanceof UtsettelsesPeriodeDto utsettelse) {
            return switch (utsettelse.type()) {
                case UTSETTELSE -> tilUtsettelsesPeriode(utsettelse, vedlegg);
                case FRI -> tilFriUtsettelsesPeriode(utsettelse, vedlegg);
            };
        }
        throw new IllegalStateException("Utviklerfeil: Ugyldig uttaksperiode " + u);
    }

    private static OverføringsPeriode tilOverføringsPeriode(OverføringsPeriodeDto u, List<VedleggDto> vedlegg) {
        return new OverføringsPeriode(
            u.fom(),
            u.tom(),
            u.årsak(),
            tilStønadskontoType(u.konto()),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererUttaksperiode(vedlegg, new ÅpenPeriodeDto(u.fom(), u.tom())))
        );
    }

    private static FriUtsettelsesPeriode tilFriUtsettelsesPeriode(UtsettelsesPeriodeDto u, List<VedleggDto> vedlegg) {
        return new FriUtsettelsesPeriode(
            u.fom(),
            u.tom(),
            u.erArbeidstaker(),
            u.årsak(),
            u.morsAktivitetIPerioden(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererUttaksperiode(vedlegg, new ÅpenPeriodeDto(u.fom(), u.tom())))
        );
    }

    private static UtsettelsesPeriode tilUtsettelsesPeriode(UtsettelsesPeriodeDto u, List<VedleggDto> vedlegg) {
        return new UtsettelsesPeriode(
            u.fom(),
            u.tom(),
            u.erArbeidstaker(),
            u.årsak(),
            u.morsAktivitetIPerioden(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererUttaksperiode(vedlegg, new ÅpenPeriodeDto(u.fom(), u.tom())))
        );
    }

    private static OppholdsPeriode tilOppholdsPeriode(OppholdsPeriodeDto u, List<VedleggDto> vedlegg) {
        return new OppholdsPeriode(
            u.fom(),
            u.tom(),
            u.årsak(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererUttaksperiode(vedlegg, new ÅpenPeriodeDto(u.fom(), u.tom())))
        );
    }

    private static UttaksPeriode tilUttaksPeriode(UttaksPeriodeDto u, List<VedleggDto> vedlegg) {
        return new UttaksPeriode(
            u.fom(),
            u.tom(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererUttaksperiode(vedlegg, new ÅpenPeriodeDto(u.fom(), u.tom()))),
            tilStønadskontoType(u.konto()),
            u.ønskerSamtidigUttak() != null && u.ønskerSamtidigUttak(),
            u.morsAktivitetIPerioden(),
            u.ønskerFlerbarnsdager() != null && u.ønskerFlerbarnsdager(),
            u.samtidigUttakProsent() != null ? ProsentAndel.valueOf(u.samtidigUttakProsent()) : null,
            u.justeresVedFødsel()
        );
    }

    private static GradertUttaksPeriode tilGradertUttaksperiode(GradertUttaksPeriodeDto u, List<VedleggDto> vedlegg) {
        return new GradertUttaksPeriode(
            u.fom(),
            u.tom(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererUttaksperiode(vedlegg, new ÅpenPeriodeDto(u.fom(), u.tom()))),
            tilStønadskontoType(u.konto()),
            u.ønskerSamtidigUttak() != null && u.ønskerSamtidigUttak(),
            u.morsAktivitetIPerioden(),
            u.ønskerFlerbarnsdager() != null && u.ønskerFlerbarnsdager(),
            u.samtidigUttakProsent() != null ? ProsentAndel.valueOf(u.samtidigUttakProsent()) : null,
            u.stillingsprosent() != null ? ProsentAndel.valueOf(u.stillingsprosent()) : null,
            u.erArbeidstaker() != null && u.erArbeidstaker(),
            u.orgnumre(),
            true,
            u.erFrilanser(),
            u.erSelvstendig(),
            u.justeresVedFødsel()
        );
    }

    private static StønadskontoType tilStønadskontoType(KontoType konto) {
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


    static AnnenForelder tilAnnenForelder(AnnenForelderDto annenForelder) {
        if (annenForelder == null) {
            return new UkjentForelder();
        }
        if (annenForelder instanceof NorskForelderDto norsk) {
            return tilNorskForelder(norsk);
        }
        if (annenForelder instanceof UtenlandskForelderDto utenlandsk) {
            return tilUtenlandskForelder(utenlandsk);
        }
        throw new IllegalStateException("Utvikerfeil: Anneforeldre er uglydig. Skal feile før dette!");
    }

    private static UtenlandskForelder tilUtenlandskForelder(UtenlandskForelderDto utenlandsk) {
        return new UtenlandskForelder(
            utenlandsk.fnr().value(),
            utenlandsk.bostedsland(),
            navn(utenlandsk.fornavn(), utenlandsk.etternavn())
        );
    }

    private static NorskForelder tilNorskForelder(NorskForelderDto norskForelder) {
        return new NorskForelder(
            norskForelder.fnr(),
            navn(norskForelder.fornavn(), norskForelder.etternavn()));
    }

    private static String navn(String fornavn, String etternavn) {
        return fornavn + " " + etternavn;
    }
}
