package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static java.time.LocalDate.now;
import static java.time.Month.OCTOBER;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.common.domain.Orgnummer;
import no.nav.foreldrepenger.common.domain.felles.InnsendingsType;
import no.nav.foreldrepenger.common.domain.felles.LukketPeriode;
import no.nav.foreldrepenger.common.domain.felles.ProsentAndel;
import no.nav.foreldrepenger.common.domain.felles.PåkrevdVedlegg;
import no.nav.foreldrepenger.common.domain.felles.VedleggMetaData;
import no.nav.foreldrepenger.common.domain.felles.medlemskap.Utenlandsopphold;
import no.nav.foreldrepenger.common.domain.felles.opptjening.AnnenOpptjening;
import no.nav.foreldrepenger.common.domain.felles.opptjening.AnnenOpptjeningType;
import no.nav.foreldrepenger.common.domain.felles.opptjening.EgenNæring;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Frilans;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Opptjening;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Regnskapsfører;
import no.nav.foreldrepenger.common.domain.felles.opptjening.UtenlandskArbeidsforhold;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Adopsjon;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.FremtidigFødsel;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Fødsel;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Omsorgsovertakelse;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.RelasjonTilBarn;
import no.nav.foreldrepenger.common.domain.felles.ÅpenPeriode;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.AnnenInntektDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.FrilansInformasjonDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.NæringDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.TilknyttetPersonDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.UtenlandsoppholdPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.Situasjon;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggInnsendingType;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggReferanse;

public final class CommonMapper {

    private CommonMapper() {
    }

    public static List<no.nav.foreldrepenger.common.domain.felles.Vedlegg> tilVedlegg(List<VedleggDto> vedlegg) {
        return safeStream(vedlegg).distinct().map(CommonMapper::tilVedlegg).toList();
    }

    public static no.nav.foreldrepenger.common.domain.felles.Vedlegg tilVedlegg(VedleggDto vedlegg) {
        var vedleggMetadata = new VedleggMetaData(tilVedleggsreferanse(vedlegg.referanse()), tilInnsendingsType(vedlegg.innsendingsType()),
            vedlegg.skjemanummer(), vedlegg.beskrivelse());
        return new PåkrevdVedlegg(vedleggMetadata);
    }

    private static InnsendingsType tilInnsendingsType(VedleggInnsendingType vedleggInnsendingType) {
        return switch (vedleggInnsendingType) {
            case LASTET_OPP -> InnsendingsType.LASTET_OPP;
            case SEND_SENERE -> InnsendingsType.SEND_SENERE;
            case AUTOMATISK -> throw new IllegalStateException("Innsendingstype AUTOMATISK skal ikke sendes ned videre");
        };
    }

    public static List<no.nav.foreldrepenger.common.domain.felles.VedleggReferanse> tilVedleggsreferanse(List<VedleggReferanse> vedleggsreferanser) {
        return safeStream(vedleggsreferanser).distinct().map(CommonMapper::tilVedleggsreferanse).toList();
    }

    public static no.nav.foreldrepenger.common.domain.felles.VedleggReferanse tilVedleggsreferanse(VedleggReferanse vedleggsreferanse) {
        return new no.nav.foreldrepenger.common.domain.felles.VedleggReferanse(vedleggsreferanse.verdi());
    }

    static Utenlandsopphold tilOppholdIUtlandet(SøknadDto s) {
        var opphold = Stream.concat(safeStream(s.informasjonOmUtenlandsopphold().tidligereOpphold()),
            safeStream(s.informasjonOmUtenlandsopphold().senereOpphold())).toList();
        return new Utenlandsopphold(tilUtenlandsoppholdsliste(opphold));
    }

    public static Opptjening tilOpptjening(SøkerDto søker, List<VedleggDto> vedlegg) {
        return new Opptjening(tilUtenlandsArbeidsforhold(søker.andreInntekterSiste10Mnd(), vedlegg),
            tilEgenNæring(søker.selvstendigNæringsdrivendeInformasjon(), vedlegg), tilAnnenOpptjening(søker.andreInntekterSiste10Mnd(), vedlegg),
            tilFrilans(søker.frilansInformasjon()));
    }

    static RelasjonTilBarn tilRelasjonTilBarn(BarnDto barn, Situasjon situasjon, List<VedleggDto> vedlegg) {
        return switch (situasjon) {
            case FØDSEL ->
                !(barn.fødselsdatoer() == null || barn.fødselsdatoer().isEmpty()) ? tilFødsel(barn, vedlegg) : tilFremtidigFødsel(barn, vedlegg);
            case ADOPSJON -> tilAdopsjon(barn, vedlegg);
            case OMSORGSOVERTAKELSE -> tilOmsorgsovertagelse(barn, vedlegg);
        };
    }

    static Fødsel tilFødsel(BarnDto barn, List<VedleggDto> vedlegg) {
        return new Fødsel(barn.antallBarn(), barn.fødselsdatoer(), barn.termindato(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererBarn(vedlegg)));
    }

    private static Omsorgsovertakelse tilOmsorgsovertagelse(BarnDto barn, List<VedleggDto> vedlegg) {
        return new Omsorgsovertakelse(barn.antallBarn(), barn.foreldreansvarsdato(), barn.fødselsdatoer(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererBarn(vedlegg)));
    }

    private static FremtidigFødsel tilFremtidigFødsel(BarnDto barn, List<VedleggDto> vedlegg) {
        return new FremtidigFødsel(barn.antallBarn(), barn.termindato(), barn.terminbekreftelseDato(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererBarn(vedlegg)));
    }

    private static Adopsjon tilAdopsjon(BarnDto barn, List<VedleggDto> vedlegg) {
        return new Adopsjon(barn.antallBarn(), barn.adopsjonsdato(), barn.adopsjonAvEktefellesBarn(), barn.søkerAdopsjonAlene(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererBarn(vedlegg)), barn.ankomstdato(), barn.fødselsdatoer());
    }

    public static List<AnnenOpptjening> tilAnnenOpptjening(List<AnnenInntektDto> andreInntekterSiste10Mnd, List<VedleggDto> vedlegg) {
        return andreInntekterSiste10Mnd.stream()
            .filter(annenInntekt -> !annenInntekt.type().equals("JOBB_I_UTLANDET"))
            .map(annenInntekt -> tilAnnenOpptjening(annenInntekt, vedlegg))
            .toList();
    }

    private static AnnenOpptjening tilAnnenOpptjening(AnnenInntektDto annenInntekt, List<VedleggDto> vedlegg) {
        return new AnnenOpptjening(annenInntekt.type() != null ? AnnenOpptjeningType.valueOf(annenInntekt.type()) : null,
            new ÅpenPeriode(annenInntekt.tidsperiode().fom(), annenInntekt.tidsperiode().tom()),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererOpptjeningsperiode(vedlegg, annenInntekt.tidsperiode())));
    }

    public static List<UtenlandskArbeidsforhold> tilUtenlandsArbeidsforhold(List<AnnenInntektDto> andreInntekterSiste10Mnd,
                                                                            List<VedleggDto> vedlegg) {
        return andreInntekterSiste10Mnd.stream()
            .filter(annenInntekt -> annenInntekt.type().equals("JOBB_I_UTLANDET"))
            .map(annenInntekt -> tilUtenlandsArbeidsforhold(annenInntekt, vedlegg))
            .toList();

    }

    private static UtenlandskArbeidsforhold tilUtenlandsArbeidsforhold(AnnenInntektDto annenInntekt, List<VedleggDto> vedlegg) {
        return new UtenlandskArbeidsforhold(annenInntekt.arbeidsgiverNavn(),
            new ÅpenPeriode(annenInntekt.tidsperiode().fom(), annenInntekt.tidsperiode().tom()),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererOpptjeningsperiode(vedlegg, annenInntekt.tidsperiode())),
            land(annenInntekt.land()));
    }

    public static List<EgenNæring> tilEgenNæring(List<NæringDto> selvstendigNæringsdrivendeInformasjon, List<VedleggDto> vedlegg) {
        return selvstendigNæringsdrivendeInformasjon.stream().map(selvstendig -> tilEgenNæring(selvstendig, vedlegg)).toList();
    }

    private static EgenNæring tilEgenNæring(NæringDto selvstendig, List<VedleggDto> vedlegg) {
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

        return new EgenNæring(selvstendig.registrertINorge() ? CountryCode.NO : land(selvstendig.registrertILand()), tilOrgnummer(selvstendig),
            selvstendig.navnPåNæringen(), selvstendig.næringstyper(),
            new ÅpenPeriode(selvstendig.tidsperiode().fom(), selvstendig.tidsperiode().tom()), nærRelasjon, regnskapsførere,
            erNyopprettet(selvstendig.tidsperiode().fom()), selvstendig.hattVarigEndringAvNæringsinntektSiste4Kalenderår(),
            selvstendig.harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene(), næringsinntektBrutto, endringsDato, selvstendig.oppstartsdato(),
            beskrivelseEndring, selvstendig.stillingsprosent() != null ? ProsentAndel.valueOf(selvstendig.stillingsprosent()) : null,
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererOpptjeningsperiode(vedlegg, selvstendig.tidsperiode())));
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

    public static Frilans tilFrilans(FrilansInformasjonDto frilansInformasjon) {
        if (frilansInformasjon == null) {
            return null;
        }
        return new Frilans(new ÅpenPeriode(frilansInformasjon.oppstart()), frilansInformasjon.jobberFremdelesSomFrilans());
    }

    private static List<Utenlandsopphold.Opphold> tilUtenlandsoppholdsliste(List<UtenlandsoppholdPeriodeDto> tidligereOpphold) {
        return safeStream(tidligereOpphold).map(CommonMapper::tilUtenlandsopphold).toList();
    }

    private static Utenlandsopphold.Opphold tilUtenlandsopphold(UtenlandsoppholdPeriodeDto o) {
        return new Utenlandsopphold.Opphold(land(o.land()), new LukketPeriode(o.tidsperiode().fom(), o.tidsperiode().tom()));
    }

    public static CountryCode land(String land) {
        return land == null || land.isEmpty() ? CountryCode.UNDEFINED : CountryCode.valueOf(land);
    }

    public static boolean erNyopprettet(LocalDate fom) {
        return erNyopprettet(LocalDate.now(), fom);
    }

    static boolean erNyopprettet(LocalDate nå, LocalDate fom) {
        return fom.isAfter(now().minusYears(nå.isAfter(LocalDate.of(nå.getYear(), OCTOBER, 20)) ? 3 : 4).with(firstDayOfYear()).minusDays(1));
    }
}
