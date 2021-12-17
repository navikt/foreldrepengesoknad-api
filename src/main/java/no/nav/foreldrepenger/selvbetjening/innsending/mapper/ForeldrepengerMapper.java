package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilAnnenForelder;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilMedlemskap;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilOpptjening;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilRelasjonTilBarn;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
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
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Foreldrepengesøknad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.UttaksplanPeriode;

final class ForeldrepengerMapper {

    private ForeldrepengerMapper() {
    }

    static no.nav.foreldrepenger.common.domain.Søknad tilForeldrepengesøknad(Foreldrepengesøknad foreldrepengesøknad) {
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

    private static Søker tilSøker(Foreldrepengesøknad f) {
        var søker = f.getSøker();
        if (søker == null) {
            throw new IllegalStateException("Kan ikke ha tom søkerobjekt");
        }
        return new Søker(
            søker.getRolle() != null ? BrukerRolle.valueOf(søker.getRolle()) : null,
            søker.getSpråkkode() != null ? Målform.valueOf(søker.getSpråkkode()) : null);
    }


    private static Foreldrepenger tilYtelse(Foreldrepengesøknad f) {
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

    private static Rettigheter tilRettigheter(Foreldrepengesøknad f) {
        return new Rettigheter(
            toBoolean(f.getAnnenForelder().getHarRettPåForeldrepenger()),
            false, // TODO: Ikke satt av api. Ikke brukt? Hva er default i mottak?
            toBoolean(f.getSøker().getErAleneOmOmsorg()),
            f.getAnnenForelder().getDatoForAleneomsorg());
    }


    private static Fordeling tilFordeling(Foreldrepengesøknad f) {
        return Fordeling.builder()
            .perioder(tilLukketPeriodeMedVedlegg(f.getUttaksplan()))
            .erAnnenForelderInformert(toBoolean(f.getAnnenForelder().getErInformertOmSøknaden()))
            // .ønskerKvoteOverført(null) // TODO: Ikke satt av api. Ikke brukt? Hva er default i mottak?
            .build();
    }

    private static List<LukketPeriodeMedVedlegg> tilLukketPeriodeMedVedlegg(List<UttaksplanPeriode> uttaksplan) {
        return uttaksplan.stream()
            .map(ForeldrepengerMapper::tilLukketPeriodeMedVedlegg)
            .toList();
    }

    private static LukketPeriodeMedVedlegg tilLukketPeriodeMedVedlegg(UttaksplanPeriode u) {
        if (Boolean.TRUE.equals(u.getGradert()) || u.getType().equalsIgnoreCase("gradert")) {
            return tilGradertUttaksperiode(u);
        }
        return switch (u.getType()) {
            case "uttak" -> tilUttaksPeriode(u);
            case "opphold" -> tilOppholdsPeriode(u);
            case "periodeUtenUttak" -> tilFriUtsettelsesPeriode(u); // periodeUtenUttak er ekvivalent med "fri" i mottak
            case "utsettelse" -> tilUtsettelsesPeriode(u);
            case "overføring" -> tilOverføringsPeriode(u);
            default -> throw new IllegalStateException("Ikke støttet periodetype: " + u.getType());
        };
    }

    private static OverføringsPeriode tilOverføringsPeriode(UttaksplanPeriode u) {
        return OverføringsPeriode.builder()
            .årsak(u.getÅrsak() != null ? Overføringsårsak.valueOf(u.getÅrsak()) : null)
            .uttaksperiodeType(u.getKonto() != null ? StønadskontoType.valueOf(u.getKonto()) : null)
            .fom(u.getTidsperiode().getFom())
            .tom(u.getTidsperiode().getTom())
            .vedlegg(u.getVedlegg())
            .build();
    }

    private static FriUtsettelsesPeriode tilFriUtsettelsesPeriode(UttaksplanPeriode u) {
        return FriUtsettelsesPeriode.FriUtsettelsesPeriodeBuilder()
            .årsak(u.getÅrsak() != null ? UtsettelsesÅrsak.valueOf(u.getÅrsak()) : null)
            .type(u.getKonto() != null ? StønadskontoType.valueOf(u.getKonto()) : null)
            .erArbeidstaker(toBoolean(u.getErArbeidstaker()))
            // .virksomhetsnummer() Ikke satt
            .morsAktivitetsType(u.getMorsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.getMorsAktivitetIPerioden()) : null)
            .fom(u.getTidsperiode().getFom())
            .tom(u.getTidsperiode().getTom())
            .vedlegg(u.getVedlegg())
            .build();

    }

    private static UtsettelsesPeriode tilUtsettelsesPeriode(UttaksplanPeriode u) {
        return UtsettelsesPeriode.UtsettelsesPeriodeBuilder()
            .årsak(u.getÅrsak() != null ? UtsettelsesÅrsak.valueOf(u.getÅrsak()) : null)
            .uttaksperiodeType(u.getKonto() != null ? StønadskontoType.valueOf(u.getKonto()) : null)
            .erArbeidstaker(toBoolean(u.getErArbeidstaker()))
            .virksomhetsnummer(u.getOrgnumre())
            .morsAktivitetsType(u.getMorsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.getMorsAktivitetIPerioden()) : null)
            .fom(u.getTidsperiode().getFom())
            .tom(u.getTidsperiode().getTom())
            .vedlegg(u.getVedlegg())
            .build();
    }

    private static OppholdsPeriode tilOppholdsPeriode(UttaksplanPeriode u) {
        return OppholdsPeriode.builder()
            .årsak(u.getÅrsak() != null ? Oppholdsårsak.valueOf(u.getÅrsak()) : null)
            .fom(u.getTidsperiode().getFom())
            .tom(u.getTidsperiode().getTom())
            .vedlegg(u.getVedlegg())
            .build();
    }

    private static UttaksPeriode tilUttaksPeriode(UttaksplanPeriode u) {
        return UttaksPeriode.UttaksPeriodeBuilder()
            .uttaksperiodeType(u.getKonto() != null ? StønadskontoType.valueOf(u.getKonto()) : null)
            .ønskerSamtidigUttak(toBoolean(u.getØnskerSamtidigUttak()))
            .morsAktivitetsType(u.getMorsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.getMorsAktivitetIPerioden()) : null)
            .ønskerFlerbarnsdager(toBoolean(u.getØnskerFlerbarnsdager()))
            .samtidigUttakProsent(u.getSamtidigUttakProsent() != null ? new ProsentAndel(u.getSamtidigUttakProsent()) : null)
            .fom(u.getTidsperiode().getFom())
            .tom(u.getTidsperiode().getTom())
            .vedlegg(u.getVedlegg())
            .build();
    }

    private static GradertUttaksPeriode tilGradertUttaksperiode(UttaksplanPeriode u) {
        return GradertUttaksPeriode.GradertUttaksPeriodeBuilder()
            .arbeidsForholdSomskalGraderes(true)
            .arbeidstidProsent(u.getStillingsprosent() != null ? new ProsentAndel(u.getStillingsprosent()) : null)
            .erArbeidstaker(toBoolean(u.getErArbeidstaker()))
            .virksomhetsnummer(u.getOrgnumre())
            .frilans(toBoolean(u.getErFrilanser()))
            .selvstendig(toBoolean(u.getErSelvstendig()))

            .uttaksperiodeType(u.getKonto() != null ? StønadskontoType.valueOf(u.getKonto()) : null)
            .ønskerSamtidigUttak(toBoolean(u.getØnskerSamtidigUttak()))
            .morsAktivitetsType(u.getMorsAktivitetIPerioden() != null ? MorsAktivitet.valueOf(u.getMorsAktivitetIPerioden()) : null)
            .ønskerFlerbarnsdager(toBoolean(u.getØnskerFlerbarnsdager()))
            .samtidigUttakProsent(u.getSamtidigUttakProsent() != null ? new ProsentAndel(u.getSamtidigUttakProsent()) : null)

            .fom(u.getTidsperiode().getFom())
            .tom(u.getTidsperiode().getTom())
            .vedlegg(u.getVedlegg())
            .build();
    }
}
