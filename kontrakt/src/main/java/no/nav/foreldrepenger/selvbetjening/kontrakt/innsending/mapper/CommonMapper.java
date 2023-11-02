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
import no.nav.foreldrepenger.common.domain.felles.DokumentType;
import no.nav.foreldrepenger.common.domain.felles.InnsendingsType;
import no.nav.foreldrepenger.common.domain.felles.LukketPeriode;
import no.nav.foreldrepenger.common.domain.felles.ProsentAndel;
import no.nav.foreldrepenger.common.domain.felles.PåkrevdVedlegg;
import no.nav.foreldrepenger.common.domain.felles.VedleggMetaData;
import no.nav.foreldrepenger.common.domain.felles.VedleggReferanse;
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
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.NæringDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.TilknyttetPersonDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.UtenlandsoppholdPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.Situasjon;

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
            vedlegg.getInnsendingsType() != null ? InnsendingsType.valueOf(vedlegg.getInnsendingsType()) : null,
            vedlegg.getSkjemanummer() != null ? DokumentType.valueOf(vedlegg.getSkjemanummer()) : null,
            vedlegg.getBeskrivelse()
        );
        return new PåkrevdVedlegg(vedleggMetadata);
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

    static Utenlandsopphold tilOppholdIUtlandet(SøknadDto s) {
        var opphold = Stream.concat(
            safeStream(s.informasjonOmUtenlandsopphold().tidligereOpphold()),
            safeStream(s.informasjonOmUtenlandsopphold().senereOpphold())
        ).toList();
        return new Utenlandsopphold(tilUtenlandsoppholdsliste(opphold));
    }

    public static Opptjening tilOpptjening(SøkerDto søker) {
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
            tilVedleggsreferanse(barn.getAlleVedlegg())
        );
    }

    private static Omsorgsovertakelse tilOmsorgsovertagelse(BarnDto barn) {
        return new Omsorgsovertakelse(
            barn.antallBarn(),
            barn.foreldreansvarsdato(),
            barn.fødselsdatoer(),
            tilVedleggsreferanse(barn.getAlleVedlegg())
        );
    }

    private static FremtidigFødsel tilFremtidigFødsel(BarnDto barn) {
        return new FremtidigFødsel(
            barn.antallBarn(),
            barn.termindato(),
            barn.terminbekreftelseDato(),
            tilVedleggsreferanse(barn.getAlleVedlegg())
        );
    }

    private static Adopsjon tilAdopsjon(BarnDto barn) {
        return new Adopsjon(
            barn.antallBarn(),
            barn.adopsjonsdato(),
            barn.adopsjonAvEktefellesBarn(),
            barn.søkerAdopsjonAlene(),
            tilVedleggsreferanse(barn.getAlleVedlegg()),
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
            tilVedleggsreferanse(annenInntekt.vedlegg()));
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
            tilVedleggsreferanse(annenInntekt.vedlegg()),
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
            tilVedleggsreferanse(selvstendig.vedlegg())
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
        if (frilansInformasjon == null) {
            return null;
        }
        return new Frilans(new ÅpenPeriode(frilansInformasjon.oppstart()), frilansInformasjon.jobberFremdelesSomFrilans());
    }

    private static List<Utenlandsopphold.Opphold> tilUtenlandsoppholdsliste(List<UtenlandsoppholdPeriodeDto> tidligereOpphold) {
        return safeStream(tidligereOpphold)
            .map(CommonMapper::tilUtenlandsopphold)
            .toList();
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
        return fom.isAfter(now().minusYears(nå.isAfter(LocalDate.of(nå.getYear(), OCTOBER, 20)) ? 3 : 4)
            .with(firstDayOfYear()).minusDays(1));
    }
}
