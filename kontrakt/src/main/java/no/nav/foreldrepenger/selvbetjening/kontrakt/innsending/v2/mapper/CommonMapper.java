package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper;

import static java.time.LocalDate.now;
import static java.time.Month.OCTOBER;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedleggsreferanse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.common.domain.felles.LukketPeriode;
import no.nav.foreldrepenger.common.domain.felles.medlemskap.Utenlandsopphold;
import no.nav.foreldrepenger.common.domain.felles.opptjening.AnnenOpptjening;
import no.nav.foreldrepenger.common.domain.felles.opptjening.AnnenOpptjeningType;
import no.nav.foreldrepenger.common.domain.felles.opptjening.EgenNæring;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Frilans;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Opptjening;
import no.nav.foreldrepenger.common.domain.felles.opptjening.UtenlandskArbeidsforhold;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Adopsjon;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.FremtidigFødsel;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Fødsel;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Omsorgsovertakelse;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.RelasjonTilBarn;
import no.nav.foreldrepenger.common.domain.felles.ÅpenPeriode;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ÅpenPeriodeDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.DokumentasjonReferanseMapper;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.AdopsjonDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.AnnenInntektDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.FrilansDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.FødselDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.NæringDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.OmsorgsovertakelseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.TerminDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.UtenlandsoppholdsperiodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;

public final class CommonMapper {

    private CommonMapper() {
    }

    static Utenlandsopphold tilOppholdIUtlandet(List<UtenlandsoppholdsperiodeDto> perioder) {
        return new Utenlandsopphold(tilUtenlandsopphold(perioder));
    }

    static List<Utenlandsopphold.Opphold> tilUtenlandsopphold(List<UtenlandsoppholdsperiodeDto> perioder) {
        return safeStream(perioder).map(CommonMapper::tilUtenlandsopphold).toList();
    }

    private static Utenlandsopphold.Opphold tilUtenlandsopphold(UtenlandsoppholdsperiodeDto oppholdsperiode) {
        return new Utenlandsopphold.Opphold(oppholdsperiode.landkode(), new LukketPeriode(oppholdsperiode.fom(), oppholdsperiode.tom()));
    }

    static RelasjonTilBarn tilRelasjonTilBarn(BarnDto barn, List<VedleggDto> vedlegg) {
        if (barn instanceof FødselDto f) {
            return tilFødsel(f, vedlegg);
        } else if (barn instanceof TerminDto t) {
            return tilFremtidigFødsel(t, vedlegg);
        } else if (barn instanceof AdopsjonDto a) {
            return tilAdopsjon(a, vedlegg);
        } else if (barn instanceof OmsorgsovertakelseDto o) {
            return tilOmsorgsovertagelse(o, vedlegg);
        } else {
            throw new IllegalStateException(
                "Utviklerfeil: Skal ikke være mulig med annen type barn enn fødsel, termin, adopsjon eller omsorgsovertakelse");
        }
    }

    private static Fødsel tilFødsel(FødselDto barn, List<VedleggDto> vedlegg) {
        return new Fødsel(barn.antallBarn(), List.of(barn.fødselsdato()), // TODO: Fjern liste i mottak!
            barn.termindato(), tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererBarn(vedlegg)));
    }

    private static FremtidigFødsel tilFremtidigFødsel(TerminDto barn, List<VedleggDto> vedlegg) {
        return new FremtidigFødsel(barn.antallBarn(),
            barn.termindato(),
            barn.terminbekreftelseDato(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererBarn(vedlegg)));
    }

    private static Omsorgsovertakelse tilOmsorgsovertagelse(OmsorgsovertakelseDto barn, List<VedleggDto> vedlegg) {
        return new Omsorgsovertakelse(barn.antallBarn(),
            barn.foreldreansvarsdato(),
            barn.fødselsdatoer(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererBarn(vedlegg)));
    }

    private static Adopsjon tilAdopsjon(AdopsjonDto barn, List<VedleggDto> vedlegg) {
        return new Adopsjon(barn.antallBarn(),
            barn.adopsjonsdato(),
            barn.adopsjonAvEktefellesBarn(),
            barn.søkerAdopsjonAlene() != null && barn.søkerAdopsjonAlene(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererBarn(vedlegg)),
            barn.ankomstdato(),
            barn.fødselsdatoer());
    }

    public static Opptjening tilOpptjening(NæringDto egenNæring,
                                           FrilansDto frilans,
                                           List<AnnenInntektDto> annenOpptjening,
                                           List<VedleggDto> vedlegg) {
        return new Opptjening(tilUtenlandsArbeidsforhold(annenOpptjening, vedlegg),
            tilEgenNæring(egenNæring, vedlegg),
            tilAnnenOpptjening(annenOpptjening, vedlegg),
            tilFrilans(frilans));
    }

    private static List<AnnenOpptjening> tilAnnenOpptjening(List<AnnenInntektDto> andreInntekterSiste10Mnd, List<VedleggDto> vedlegg) {
        return andreInntekterSiste10Mnd.stream()
            .filter(a -> !AnnenOpptjeningType.JOBB_I_UTLANDET.equals(a.type()))
            .map(a -> tilAnnenOpptjening(a, vedlegg))
            .toList();
    }

    private static AnnenOpptjening tilAnnenOpptjening(AnnenInntektDto annenInntekt, List<VedleggDto> vedlegg) {
        return new AnnenOpptjening(annenInntekt.type(),
            new ÅpenPeriode(annenInntekt.fom(), annenInntekt.tom()),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererOpptjeningsperiode(vedlegg,
                new ÅpenPeriodeDtoOLD(annenInntekt.fom(), annenInntekt.tom())))); // TODO ÅpenPeriode
    }

    private static List<UtenlandskArbeidsforhold> tilUtenlandsArbeidsforhold(List<AnnenInntektDto> andreInntekterSiste10Mnd,
                                                                             List<VedleggDto> vedlegg) {
        return andreInntekterSiste10Mnd.stream()
            .filter(u -> AnnenOpptjeningType.JOBB_I_UTLANDET.equals(u.type()))
            .map(u -> tilUtenlandsArbeidsforhold(u, vedlegg))
            .toList();

    }

    private static UtenlandskArbeidsforhold tilUtenlandsArbeidsforhold(AnnenInntektDto annenInntekt, List<VedleggDto> vedlegg) {
        return new UtenlandskArbeidsforhold(annenInntekt.arbeidsgiverNavn(),
            new ÅpenPeriode(annenInntekt.fom(), annenInntekt.tom()),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererOpptjeningsperiode(vedlegg,
                new ÅpenPeriodeDtoOLD(annenInntekt.fom(), annenInntekt.tom()))),
            annenInntekt.land());
    }

    private static List<EgenNæring> tilEgenNæring(NæringDto selvstendig, List<VedleggDto> vedlegg) {
        if (selvstendig == null) {
            return List.of();
        }
        var næringsinntektBrutto = Optional.ofNullable(selvstendig.varigEndringInntektEtterEndring())
            .map(Integer::longValue)
            .orElse(Optional.ofNullable(selvstendig.næringsinntekt()).map(Integer::longValue).orElse(0L));

        return List.of(new EgenNæring( // TODO: Aldri mer enn 1 dokument
            selvstendig.registrertINorge() ? CountryCode.NO : selvstendig.registrertILand(),
            selvstendig.organisasjonsnummer(),
            selvstendig.navnPåNæringen(),
            List.of(selvstendig.næringstype()),
            new ÅpenPeriode(selvstendig.fom(), selvstendig.tom()),
            false,
            // TODO: aldri oppgitt
            null,
            erNyopprettet(selvstendig.fom()),
            selvstendig.hattVarigEndringAvNæringsinntektSiste4Kalenderår(),
            selvstendig.harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene(),
            næringsinntektBrutto,
            selvstendig.varigEndringDato(),
            selvstendig.oppstartsdato(),
            selvstendig.varigEndringBeskrivelse(),
            null,
            // TODO: Stillingsprosent ikke i bruk
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererOpptjeningsperiode(vedlegg,
                new ÅpenPeriodeDtoOLD(selvstendig.fom(), selvstendig.tom())))));
    }


    public static Frilans tilFrilans(FrilansDto frilansInformasjon) {
        if (frilansInformasjon == null) {
            return null;
        }
        return new Frilans(new ÅpenPeriode(frilansInformasjon.oppstart()), frilansInformasjon.jobberFremdelesSomFrilans());
    }

    public static boolean erNyopprettet(LocalDate fom) {
        return erNyopprettet(LocalDate.now(), fom);
    }

    static boolean erNyopprettet(LocalDate nå, LocalDate fom) {
        return fom.isAfter(now().minusYears(nå.isAfter(LocalDate.of(nå.getYear(), OCTOBER, 20)) ? 3 : 4).with(firstDayOfYear()).minusDays(1));
    }
}
