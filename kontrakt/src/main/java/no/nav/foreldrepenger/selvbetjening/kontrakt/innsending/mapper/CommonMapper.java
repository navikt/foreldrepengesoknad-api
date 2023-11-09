package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static java.time.LocalDate.now;
import static java.time.Month.OCTOBER;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static no.nav.foreldrepenger.common.domain.felles.VedleggMetaData.Dokumenterer.Type.TILRETTELEGGING;
import static no.nav.foreldrepenger.common.domain.felles.VedleggMetaData.Dokumenterer.Type.UTTAK;
import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.common.domain.Orgnummer;
import no.nav.foreldrepenger.common.domain.felles.DokumentType;
import no.nav.foreldrepenger.common.domain.felles.InnsendingsType;
import no.nav.foreldrepenger.common.domain.felles.LukketPeriode;
import no.nav.foreldrepenger.common.domain.felles.ProsentAndel;
import no.nav.foreldrepenger.common.domain.felles.PåkrevdVedlegg;
import no.nav.foreldrepenger.common.domain.felles.VedleggMetaData;
import no.nav.foreldrepenger.common.domain.felles.VedleggReferanse;
import no.nav.foreldrepenger.common.domain.felles.medlemskap.Medlemsskap;
import no.nav.foreldrepenger.common.domain.felles.medlemskap.Utenlandsopphold;
import no.nav.foreldrepenger.common.domain.felles.opptjening.AnnenOpptjening;
import no.nav.foreldrepenger.common.domain.felles.opptjening.AnnenOpptjeningType;
import no.nav.foreldrepenger.common.domain.felles.opptjening.EgenNæring;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Frilans;
import no.nav.foreldrepenger.common.domain.felles.opptjening.FrilansOppdrag;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Opptjening;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Regnskapsfører;
import no.nav.foreldrepenger.common.domain.felles.opptjening.UtenlandskArbeidsforhold;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Adopsjon;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.FremtidigFødsel;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Fødsel;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Omsorgsovertakelse;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.RelasjonTilBarn;
import no.nav.foreldrepenger.common.domain.felles.ÅpenPeriode;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.arbeidsforhold.Arbeidsforhold;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.AnnenInntektDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.FrilansInformasjonDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.FrilansoppdragDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.NæringDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.TilknyttetPersonDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.UtenlandsoppholdPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.Situasjon;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.ArbeidsforholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ÅpenPeriodeDto;

public final class CommonMapper {

    private CommonMapper() {
    }

    public static List<no.nav.foreldrepenger.common.domain.felles.Vedlegg> tilVedlegg(List<VedleggDto> vedlegg) {
        return safeStream(vedlegg)
            .distinct()
            .map(CommonMapper::tilVedlegg)
            .toList();
    }

    public static no.nav.foreldrepenger.common.domain.felles.Vedlegg tilVedlegg(VedleggDto vedlegg) {
        var vedleggMetadata = new VedleggMetaData(
            tilVedleggsreferanse(vedlegg.getId()),
            tilUuid(vedlegg.getUuid()),
            vedlegg.getInnsendingsType() != null ? InnsendingsType.valueOf(vedlegg.getInnsendingsType()) : null,
            vedlegg.getSkjemanummer() != null ? DokumentType.valueOf(vedlegg.getSkjemanummer()) : null,
            vedlegg.getFilename(),
            tilDokumenterer(vedlegg.getDokumenterer()),
            vedlegg.getBeskrivelse()
        );
        return new PåkrevdVedlegg(vedleggMetadata);
    }

    private static UUID tilUuid(String uuid) {
        if (uuid == null) {
            return UUID.randomUUID();
        }
        return UUID.fromString(uuid);
    }

    private static VedleggMetaData.Dokumenterer tilDokumenterer(VedleggDto.Dokumenterer hvaDokumentererVedlegg) {
        if (hvaDokumentererVedlegg == null) {
            return null;
        }
        return switch (hvaDokumentererVedlegg.type()) {
            case UTTAK -> tilDokumentererUttak(hvaDokumentererVedlegg);
            case TILRETTELEGGING -> tilDokumentererTilrettelegging(hvaDokumentererVedlegg);
        };
    }

    private static VedleggMetaData.Dokumenterer tilDokumentererUttak(VedleggDto.Dokumenterer hvaDokumentererVedlegg) {
        return new VedleggMetaData.Dokumenterer(
            tilType(hvaDokumentererVedlegg.type()),
            null,
            tilPerioder(hvaDokumentererVedlegg.perioder())
        );
    }

    private static VedleggMetaData.Dokumenterer tilDokumentererTilrettelegging(VedleggDto.Dokumenterer hvaDokumentererVedlegg) {
        return new VedleggMetaData.Dokumenterer(
            tilType(hvaDokumentererVedlegg.type()),
            tilArbeidsforholdSvp(hvaDokumentererVedlegg.arbeidsforhold()),
            null
        );
    }

    private static List<LukketPeriode> tilPerioder(List<ÅpenPeriodeDto> perioder) {
        return perioder.stream()
            .map(CommonMapper::tilLukketPeriode)
            .toList();
    }

    private static LukketPeriode tilLukketPeriode(ÅpenPeriodeDto periode) {
        return new LukketPeriode(periode.fom(), periode.tom());
    }

    private static Arbeidsforhold tilArbeidsforholdSvp(ArbeidsforholdDto arbeidsforhold) {
        return SvangerskapspengerMapper.tilArbeidsforhold(arbeidsforhold);
    }

    private static VedleggMetaData.Dokumenterer.Type tilType(VedleggDto.Dokumenterer.Type type) {
        return switch (type) {
            case UTTAK -> UTTAK;
            case TILRETTELEGGING -> TILRETTELEGGING;
        };
    }

    public static List<VedleggReferanse> tilVedleggsreferanse(List<MutableVedleggReferanseDto> vedleggsreferanser) {
        return safeStream(vedleggsreferanser)
                .distinct()
                .map(CommonMapper::tilVedleggsreferanse)
                .toList();
    }

    public static VedleggReferanse tilVedleggsreferanse(MutableVedleggReferanseDto vedleggsreferanse) {
        return new VedleggReferanse(vedleggsreferanse.referanse());
    }

    static Medlemsskap tilMedlemskap(SøknadDto s) {
        var opphold = s.informasjonOmUtenlandsopphold();
        return new Medlemsskap(
            tilUtenlandsoppholdsliste(opphold.tidligereOpphold()),
            tilUtenlandsoppholdsliste(opphold.senereOpphold()));
    }

    static Opptjening tilOpptjening(SøknadDto s) {
        var søker = s.søker();
        return new Opptjening(
            tilUtenlandsArbeidsforhold(søker.andreInntekterSiste10Mnd()),
            tilEgenNæring(søker.selvstendigNæringsdrivendeInformasjon()),
            tilAnnenOpptjening(søker.andreInntekterSiste10Mnd()),
            tilFrilans(søker.frilansInformasjon())
        );
    }

    static RelasjonTilBarn tilRelasjonTilBarn(BarnDto barn, Situasjon situasjon) {
        return switch (situasjon) {
            case FØDSEL -> !(barn.fødselsdatoer() == null || barn.fødselsdatoer().isEmpty()) ? tilFødsel(barn) : tilFremtidigFødsel(barn);
            case ADOPSJON -> tilAdopsjon(barn);
            case OMSORGSOVERTAKELSE -> tilOmsorgsovertagelse(barn);
        };
    }

    static Fødsel tilFødsel(BarnDto barn) {
        return new Fødsel(
            barn.antallBarn(),
            barn.fødselsdatoer(),
            barn.termindato(),
            List.of()
        );
    }

    private static Omsorgsovertakelse tilOmsorgsovertagelse(BarnDto barn) {
        return new Omsorgsovertakelse(
            barn.antallBarn(),
            barn.foreldreansvarsdato(),
            barn.fødselsdatoer(),
            List.of()
        );
    }

    private static FremtidigFødsel tilFremtidigFødsel(BarnDto barn) {
        return new FremtidigFødsel(
            barn.antallBarn(),
            barn.termindato(),
            barn.terminbekreftelseDato(),
            List.of()
        );
    }

    private static Adopsjon tilAdopsjon(BarnDto barn) {
        return new Adopsjon(
            barn.antallBarn(),
            barn.adopsjonsdato(),
            barn.adopsjonAvEktefellesBarn(),
            barn.søkerAdopsjonAlene(),
            List.of(),
            barn.ankomstdato(),
            barn.fødselsdatoer()
        );
    }

    private static List<AnnenOpptjening> tilAnnenOpptjening(List<AnnenInntektDto> andreInntekterSiste10Mnd) {
        return andreInntekterSiste10Mnd.stream()
            .filter(annenInntekt -> !annenInntekt.type().equals("JOBB_I_UTLANDET"))
            .map(CommonMapper::tilAnnenOpptjening)
            .toList();
    }

    private static AnnenOpptjening tilAnnenOpptjening(AnnenInntektDto annenInntekt) {
        return new AnnenOpptjening(
            annenInntekt.type() != null ? AnnenOpptjeningType.valueOf(annenInntekt.type()) : null,
            new ÅpenPeriode(annenInntekt.tidsperiode().fom(), annenInntekt.tidsperiode().tom()),
            List.of());
    }

    private static List<UtenlandskArbeidsforhold> tilUtenlandsArbeidsforhold(List<AnnenInntektDto> andreInntekterSiste10Mnd) {
        return andreInntekterSiste10Mnd.stream()
            .filter(annenInntekt -> annenInntekt.type().equals("JOBB_I_UTLANDET"))
            .map(CommonMapper::tilUtenlandsArbeidsforhold)
            .toList();

    }

    private static UtenlandskArbeidsforhold tilUtenlandsArbeidsforhold(AnnenInntektDto annenInntekt) {
        return new UtenlandskArbeidsforhold(
            annenInntekt.arbeidsgiverNavn(),
            new ÅpenPeriode(annenInntekt.tidsperiode().fom(), annenInntekt.tidsperiode().tom()),
            List.of(),
            land(annenInntekt.land())
        );
    }

    private static List<EgenNæring> tilEgenNæring(List<NæringDto> selvstendigNæringsdrivendeInformasjon) {
        return selvstendigNæringsdrivendeInformasjon.stream()
            .map(CommonMapper::tilEgenNæring)
            .toList();
    }

    private static EgenNæring tilEgenNæring(NæringDto selvstendig) {
        var nærRelasjon = false; // TODO: Kan vi ha både regnsskapsfører og revisor? Vi har bare propagert en av delene til nå. Bare regnskapsfører hvis begge er oppgitt.
        List<Regnskapsfører> regnskapsførere = null;
        var regnskapsfører = selvstendig.regnskapsfører();
        var revisor = selvstendig.revisor();
        if (regnskapsfører != null) {
            regnskapsførere = List.of(tilRegnskapsfører(regnskapsfører));
            nærRelasjon = regnskapsfører.erNærVennEllerFamilie();
        } else if (revisor != null) {
            regnskapsførere = List.of(tilRegnskapsfører(revisor));
            nærRelasjon = revisor.erNærVennEllerFamilie();
        }

        LocalDate endringsDato = null;
        String beskrivelseEndring = null;
        var næringsinntektBrutto = selvstendig.næringsinntekt();
        var næringsInfo = selvstendig.endringAvNæringsinntektInformasjon();
        if (næringsInfo != null) {
            endringsDato = næringsInfo.dato();
            næringsinntektBrutto = næringsInfo.næringsinntektEtterEndring();
            beskrivelseEndring = næringsInfo.forklaring();
        }

        return new EgenNæring(
            selvstendig.registrertINorge() ? CountryCode.NO : land(selvstendig.registrertILand()),
            tilOrgnummer(selvstendig),
            selvstendig.navnPåNæringen(),
            selvstendig.næringstyper(),
            new ÅpenPeriode(selvstendig.tidsperiode().fom(), selvstendig.tidsperiode().tom()),
            nærRelasjon,
            regnskapsførere,
            erNyopprettet(selvstendig.tidsperiode().fom()),
            selvstendig.hattVarigEndringAvNæringsinntektSiste4Kalenderår(),
            selvstendig.harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene(),
            næringsinntektBrutto,
            endringsDato,
            selvstendig.oppstartsdato(),
            beskrivelseEndring,
            selvstendig.stillingsprosent() != null ? ProsentAndel.valueOf(selvstendig.stillingsprosent()) : null,
            List.of()
        );
    }

    private static Orgnummer tilOrgnummer(NæringDto selvstendig) {
        if (selvstendig.registrertINorge()) {
            return selvstendig.organisasjonsnummer() != null ? new Orgnummer(selvstendig.organisasjonsnummer()) : null;
        }
        return null;
    }

    private static Regnskapsfører tilRegnskapsfører(TilknyttetPersonDto person) {
        return new Regnskapsfører(person.navn(), person.telefonnummer());
    }

    private static Frilans tilFrilans(FrilansInformasjonDto frilansInformasjon) {
        if (frilansInformasjon == null)  {
            return null;
        }
        return new Frilans(
            new ÅpenPeriode(frilansInformasjon.oppstart()),
            frilansInformasjon.driverFosterhjem(),
            frilansInformasjon.oppstart().isAfter(now().minusMonths(3)),
            frilansInformasjon.jobberFremdelesSomFrilans(),
            tilFrilansOppdrag(frilansInformasjon.oppdragForNæreVennerEllerFamilieSiste10Mnd()));
    }

    private static List<FrilansOppdrag> tilFrilansOppdrag(List<FrilansoppdragDto> oppdragForNæreVennerEllerFamilieSiste10Mnd) {
        return safeStream(oppdragForNæreVennerEllerFamilieSiste10Mnd)
            .map(CommonMapper::tilFrilansOppdrag)
            .toList();
    }

    private static FrilansOppdrag tilFrilansOppdrag(FrilansoppdragDto o) {
        return new FrilansOppdrag(o.navnPåArbeidsgiver(), new ÅpenPeriode(o.tidsperiode().fom(), o.tidsperiode().tom()));
    }

    private static List<Utenlandsopphold> tilUtenlandsoppholdsliste(List<UtenlandsoppholdPeriodeDto> tidligereOpphold) {
        return safeStream(tidligereOpphold)
            .map(CommonMapper::tilUtenlandsopphold)
            .toList();
    }

    private static Utenlandsopphold tilUtenlandsopphold(UtenlandsoppholdPeriodeDto o) {
        return new Utenlandsopphold(land(o.land()), new LukketPeriode(o.tidsperiode().fom(), o.tidsperiode().tom()));
    }

    public static CountryCode land(String land) {
        return land == null || land.isEmpty() ? CountryCode.UNDEFINED : CountryCode.valueOf(land);
    }

    public static boolean erNyopprettet(LocalDate fom) {
        return erNyopprettet(LocalDate.now(), fom);
    }

    static boolean erNyopprettet(LocalDate nå, LocalDate fom) {
        return fom.isAfter(now().minusYears(nå.isAfter(LocalDate.of(nå.getYear(), OCTOBER, 20)) ? 3 : 4)
            .with(firstDayOfYear()).minusDays(1));
    }
}
