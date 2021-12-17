package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;
import static java.time.LocalDate.now;
import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.selvbetjening.util.DateUtil.erNyopprettet;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDate;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Orgnummer;
import no.nav.foreldrepenger.common.domain.felles.LukketPeriode;
import no.nav.foreldrepenger.common.domain.felles.ProsentAndel;
import no.nav.foreldrepenger.common.domain.felles.annenforelder.AnnenForelder;
import no.nav.foreldrepenger.common.domain.felles.annenforelder.NorskForelder;
import no.nav.foreldrepenger.common.domain.felles.annenforelder.UkjentForelder;
import no.nav.foreldrepenger.common.domain.felles.annenforelder.UtenlandskForelder;
import no.nav.foreldrepenger.common.domain.felles.medlemskap.ArbeidsInformasjon;
import no.nav.foreldrepenger.common.domain.felles.medlemskap.FramtidigOppholdsInformasjon;
import no.nav.foreldrepenger.common.domain.felles.medlemskap.Medlemsskap;
import no.nav.foreldrepenger.common.domain.felles.medlemskap.TidligereOppholdsInformasjon;
import no.nav.foreldrepenger.common.domain.felles.medlemskap.Utenlandsopphold;
import no.nav.foreldrepenger.common.domain.felles.opptjening.AnnenOpptjening;
import no.nav.foreldrepenger.common.domain.felles.opptjening.AnnenOpptjeningType;
import no.nav.foreldrepenger.common.domain.felles.opptjening.EgenNæring;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Frilans;
import no.nav.foreldrepenger.common.domain.felles.opptjening.FrilansOppdrag;
import no.nav.foreldrepenger.common.domain.felles.opptjening.NorskOrganisasjon;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Opptjening;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Regnskapsfører;
import no.nav.foreldrepenger.common.domain.felles.opptjening.UtenlandskArbeidsforhold;
import no.nav.foreldrepenger.common.domain.felles.opptjening.UtenlandskOrganisasjon;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Virksomhetstype;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Adopsjon;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.FremtidigFødsel;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Fødsel;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Omsorgsovertakelse;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.RelasjonTilBarn;
import no.nav.foreldrepenger.common.domain.felles.ÅpenPeriode;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Barn;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.UtenlandsoppholdPeriode;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.AnnenInntekt;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.FrilansInformasjon;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.Frilansoppdrag;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.SelvstendigNæringsdrivendeInformasjon;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.TilknyttetPerson;

final class CommonMapper {

    private static final LocalDate TRE_MÅNEDER_FØR_FOM = now().minusMonths(3);

    private CommonMapper() {
    }


    static AnnenForelder tilAnnenForelder(Søknad søknad) {
        var annenForelder = søknad.getAnnenForelder();
        if (annenForelder == null) {
            return new UkjentForelder();
        }
        return switch (annenForelder.type()) {
            case "norsk" -> tilNorskForelder(annenForelder);
            case "utenlandsk" -> tilUtenlandskForelder(annenForelder);
            case "ukjent" -> new UkjentForelder();
            default -> throw new IllegalStateException("Ukjent typeverdi av annenforelder: " + annenForelder.type());
        };
    }

    static Medlemsskap tilMedlemskap(Søknad s) {
        var opphold = s.getInformasjonOmUtenlandsopphold();
        var tidligereOppholdsInfo = tilTidligereOppholdsInfo(opphold.getTidligereOpphold());
        var framtidigOppholdsInfo = tilFramtidigOppholdsinformasjon(opphold.getSenereOpphold());
        return new Medlemsskap(tidligereOppholdsInfo, framtidigOppholdsInfo);
    }

    static Opptjening tilOpptjening(Søknad s) {
        var søker = s.getSøker();
        return Opptjening.builder()
            .frilans(tilFrilans(søker.getFrilansInformasjon()))
            .egenNæring(tilEgenNæring(søker.getSelvstendigNæringsdrivendeInformasjon()))
            .utenlandskArbeidsforhold(tilUtenlandsArbeidsforhold(søker.getAndreInntekterSiste10Mnd()))
            .annenOpptjening(tilAnnenOpptjening(søker.getAndreInntekterSiste10Mnd()))
            .build();

    }

    static RelasjonTilBarn tilRelasjonTilBarn(Søknad søknad) {
        var barn = søknad.getBarn();
        if (barn.adopsjonsdato != null) {
            return tilAdopsjon(barn);
        }
        var situasjon = søknad.getSituasjon();
        if (isEmpty(situasjon) || situasjon.equals("fødsel")) {
            barn.erBarnetFødt = !CollectionUtils.isEmpty(søknad.getBarn().fødselsdatoer);
            return Boolean.TRUE.equals(barn.erBarnetFødt) ? tilFødsel(barn) : tilFremtidigFødsel(barn);
        }
        return tilOmsorgsovertagelse(barn);
    }

    static Fødsel tilFødsel(Barn barn) {
        return Fødsel.builder()
            .fødselsdato(barn.fødselsdatoer)
            .termindato(barn.termindato)
            .antallBarn(barn.antallBarn)
            .vedlegg(barn.getAlleVedlegg())
            .build();
    }

    private static Omsorgsovertakelse tilOmsorgsovertagelse(Barn barn) {
        return Omsorgsovertakelse.builder()
            .omsorgsovertakelsesdato(barn.foreldreansvarsdato)
            .årsak(null) // TODO: Ikke satt av api, heller ikke sendt ned fra frontend
            .fødselsdato(barn.fødselsdatoer)
            .antallBarn(barn.antallBarn)
            // .beskrivelse() ikke satt, ikke brukt?
            .vedlegg(barn.getAlleVedlegg())
            .build();
    }

    private static FremtidigFødsel tilFremtidigFødsel(Barn barn) {
        return FremtidigFødsel.builder()
            .terminDato(barn.termindato)
            .utstedtDato(barn.terminbekreftelseDato)
            .antallBarn(barn.antallBarn)
            .vedlegg(barn.getAlleVedlegg())
            .build();
    }

    private static Adopsjon tilAdopsjon(Barn barn) {
        return Adopsjon.builder()
            .omsorgsovertakelsesdato(barn.adopsjonsdato)
            .ektefellesBarn(toBoolean(barn.adopsjonAvEktefellesBarn))
            .søkerAdopsjonAlene(toBoolean(barn.søkerAdopsjonAlene))
            .ankomstDato(barn.ankomstdato)
            .fødselsdato(barn.fødselsdatoer)
            .antallBarn(barn.antallBarn)
            .vedlegg(barn.getAlleVedlegg())
            .build();
    }

    private static UtenlandskForelder tilUtenlandskForelder(no.nav.foreldrepenger.selvbetjening.innsending.domain.AnnenForelder annenForelder) {
        return new UtenlandskForelder(
            annenForelder.getFnr(),
            land(annenForelder.getBostedsland()),
            navn(annenForelder)
        );
    }


    private static NorskForelder tilNorskForelder(no.nav.foreldrepenger.selvbetjening.innsending.domain.AnnenForelder annenForelder) {
        return new NorskForelder(Fødselsnummer.valueOf(annenForelder.getFnr()), navn(annenForelder));
    }

    private static String navn(no.nav.foreldrepenger.selvbetjening.innsending.domain.AnnenForelder annenForelder) {
        return isNotBlank(annenForelder.getNavn()) ? annenForelder.getNavn() :
            annenForelder.getFornavn() + " " + annenForelder.getEtternavn();
    }

    private static List<AnnenOpptjening> tilAnnenOpptjening(List<AnnenInntekt> andreInntekterSiste10Mnd) {
        return andreInntekterSiste10Mnd.stream()
            .filter(annenInntekt -> !annenInntekt.getType().equals("JOBB_I_UTLANDET"))
            .map(CommonMapper::tilAnnenOpptjening)
            .toList();
    }

    private static AnnenOpptjening tilAnnenOpptjening(AnnenInntekt annenInntekt) {
        return new AnnenOpptjening(
            annenInntekt.getType() != null ? AnnenOpptjeningType.valueOf(annenInntekt.getType()) : null,
            new ÅpenPeriode(annenInntekt.getTidsperiode().getFom(), annenInntekt.getTidsperiode().getTom()),
            annenInntekt.getVedlegg());
    }

    private static List<UtenlandskArbeidsforhold> tilUtenlandsArbeidsforhold(List<AnnenInntekt> andreInntekterSiste10Mnd) {
        return andreInntekterSiste10Mnd.stream()
            .filter(annenInntekt -> annenInntekt.getType().equals("JOBB_I_UTLANDET"))
            .map(CommonMapper::tilUtenlandsArbeidsforhold)
            .toList();

    }

    private static UtenlandskArbeidsforhold tilUtenlandsArbeidsforhold(AnnenInntekt annenInntekt) {
        return UtenlandskArbeidsforhold.builder()
            .arbeidsgiverNavn(annenInntekt.getArbeidsgiverNavn())
            .land(land(annenInntekt.getLand()))
            .periode(new ÅpenPeriode(annenInntekt.getTidsperiode().getFom(), annenInntekt.getTidsperiode().getTom()))
            .vedlegg(annenInntekt.getVedlegg())
            .build();
    }

    private static List<EgenNæring> tilEgenNæring(List<SelvstendigNæringsdrivendeInformasjon> selvstendigNæringsdrivendeInformasjon) {
        return selvstendigNæringsdrivendeInformasjon.stream()
            .map(CommonMapper::tilEgenNæring)
            .toList();
    }

    private static EgenNæring tilEgenNæring(SelvstendigNæringsdrivendeInformasjon selvstendig) {
        return Boolean.TRUE.equals(selvstendig.getRegistrertINorge()) ? tilNorskNæring(selvstendig) : tilUtenlandskOrganisasjon(selvstendig);
    }

    private static NorskOrganisasjon tilNorskNæring(SelvstendigNæringsdrivendeInformasjon selvstendig) {
        // Spesifikk
        var norskOrganisasjonBuilder = NorskOrganisasjon.builder()
            .orgName(selvstendig.getNavnPåNæringen())
            .orgNummer(Orgnummer.valueOf(selvstendig.getOrganisasjonsnummer()));


        // Generelle TODO: Identisk til UtenlandskOrgansiasjon.. Mye duplisering... skrive om abstract class maybe?
        norskOrganisasjonBuilder
            .stillingsprosent(selvstendig.getStillingsprosent() != null ? new ProsentAndel(selvstendig.getStillingsprosent()) : null)
            .periode(new ÅpenPeriode(selvstendig.getTidsperiode().getFom(), selvstendig.getTidsperiode().getTom()))
            .erNyIArbeidslivet(toBoolean(selvstendig.getHarBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene()))
            .erNyOpprettet(erNyopprettet(selvstendig.getTidsperiode().getFom()))
            .erVarigEndring(toBoolean(selvstendig.getHattVarigEndringAvNæringsinntektSiste4Kalenderår()))
            .vedlegg(selvstendig.getVedlegg())
            .virksomhetsTyper(tilVirksomhetsTyper(selvstendig.getNæringstyper()))
            .oppstartsDato(selvstendig.getOppstartsdato());

        var næringsInfo = selvstendig.getEndringAvNæringsinntektInformasjon();
        if (næringsInfo != null) {
            norskOrganisasjonBuilder
                .endringsDato(næringsInfo.getDato())
                .næringsinntektBrutto(næringsInfo.getNæringsinntektEtterEndring())
                .beskrivelseEndring(næringsInfo.getForklaring());
        } else {
            norskOrganisasjonBuilder.næringsinntektBrutto(selvstendig.getNæringsinntekt());
        }

        var regnskapsfører = selvstendig.getRegnskapsfører();
        var revisor = selvstendig.getRevisor();
        if (regnskapsfører != null) {
            norskOrganisasjonBuilder
                .regnskapsførere(List.of(tilRegnskapsfører(regnskapsfører)))
                .nærRelasjon(toBoolean(regnskapsfører.getErNærVennEllerFamilie()));
        } else if (revisor != null) {
            norskOrganisasjonBuilder
                .regnskapsførere(List.of(tilRegnskapsfører(revisor)))
                .nærRelasjon(toBoolean(revisor.getErNærVennEllerFamilie()));
        }

        return norskOrganisasjonBuilder.build();
    }

    private static UtenlandskOrganisasjon tilUtenlandskOrganisasjon(SelvstendigNæringsdrivendeInformasjon selvstendig) {
        // Spesifikk
        var utenlandskOrganisasjonBuilder = UtenlandskOrganisasjon.builder()
            .orgName(selvstendig.getNavnPåNæringen())
            .registrertILand(CountryCode.valueOf(selvstendig.getRegistrertILand()));

        // Generelle
        utenlandskOrganisasjonBuilder
            .stillingsprosent(selvstendig.getStillingsprosent() != null ? new ProsentAndel(selvstendig.getStillingsprosent()) : null)
            .periode(new ÅpenPeriode(selvstendig.getTidsperiode().getFom(), selvstendig.getTidsperiode().getTom()))
            .erNyIArbeidslivet(toBoolean(selvstendig.getHarBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene()))
            .erNyOpprettet(erNyopprettet(selvstendig.getTidsperiode().getFom()))
            .erVarigEndring(toBoolean(selvstendig.getHattVarigEndringAvNæringsinntektSiste4Kalenderår()))
            .vedlegg(selvstendig.getVedlegg())
            .virksomhetsTyper(tilVirksomhetsTyper(selvstendig.getNæringstyper()))
            .oppstartsDato(selvstendig.getOppstartsdato());

        var næringsInfo = selvstendig.getEndringAvNæringsinntektInformasjon();
        if (næringsInfo != null) {
            utenlandskOrganisasjonBuilder
                .endringsDato(næringsInfo.getDato())
                .næringsinntektBrutto(næringsInfo.getNæringsinntektEtterEndring())
                .beskrivelseEndring(næringsInfo.getForklaring());
        } else {
            utenlandskOrganisasjonBuilder.næringsinntektBrutto(selvstendig.getNæringsinntekt());
        }

        var regnskapsfører = selvstendig.getRegnskapsfører();
        var revisor = selvstendig.getRevisor();
        if (regnskapsfører != null) {
            utenlandskOrganisasjonBuilder
                .regnskapsførere(List.of(tilRegnskapsfører(regnskapsfører)))
                .nærRelasjon(toBoolean(regnskapsfører.getErNærVennEllerFamilie()));
        } else if (revisor != null) {
            utenlandskOrganisasjonBuilder
                .regnskapsførere(List.of(tilRegnskapsfører(revisor)))
                .nærRelasjon(toBoolean(revisor.getErNærVennEllerFamilie()));
        }

        return utenlandskOrganisasjonBuilder.build();
    }

    private static Regnskapsfører tilRegnskapsfører(TilknyttetPerson person) {
        return new Regnskapsfører(person.getNavn(), person.getTelefonnummer());
    }

    private static List<Virksomhetstype> tilVirksomhetsTyper(List<String> næringstyper) {
        return safeStream(næringstyper)
            .map(Virksomhetstype::valueOf)
            .toList();
    }

    private static Frilans tilFrilans(FrilansInformasjon frilansInformasjon) {
        if (frilansInformasjon == null)  {
            return null;
        }
        return new Frilans(
            new ÅpenPeriode(frilansInformasjon.getOppstart()),
            toBoolean(frilansInformasjon.getDriverFosterhjem()),
            frilansInformasjon.getOppstart().isAfter(TRE_MÅNEDER_FØR_FOM),
            tilFrilansOppdrag(frilansInformasjon.getOppdragForNæreVennerEllerFamilieSiste10Mnd()),
            null); // TODO: Fjern denne som ikke blir brukt
    }

    private static List<FrilansOppdrag> tilFrilansOppdrag(List<Frilansoppdrag> oppdragForNæreVennerEllerFamilieSiste10Mnd) {
        return safeStream(oppdragForNæreVennerEllerFamilieSiste10Mnd)
            .map(CommonMapper::tilFrilansOppdrag)
            .toList();
    }

    private static FrilansOppdrag tilFrilansOppdrag(Frilansoppdrag o) {
        return new FrilansOppdrag(o.getNavnPåArbeidsgiver(), new ÅpenPeriode(o.getTidsperiode().getFom(), o.getTidsperiode().getTom()));
    }

    private static FramtidigOppholdsInformasjon tilFramtidigOppholdsinformasjon(List<UtenlandsoppholdPeriode> senereOpphold) {
        return new FramtidigOppholdsInformasjon(tilUtenlandsoppholdsliste(senereOpphold));
    }

    private static TidligereOppholdsInformasjon tilTidligereOppholdsInfo(List<UtenlandsoppholdPeriode> tidligereOpphold) {
        return new TidligereOppholdsInformasjon(ArbeidsInformasjon.IKKE_ARBEIDET, tilUtenlandsoppholdsliste(tidligereOpphold));
    }

    private static List<Utenlandsopphold> tilUtenlandsoppholdsliste(List<UtenlandsoppholdPeriode> tidligereOpphold) {
        return safeStream(tidligereOpphold)
            .map(CommonMapper::tilUtenlandsopphold)
            .toList();
    }

    private static Utenlandsopphold tilUtenlandsopphold(UtenlandsoppholdPeriode o) {
        return new Utenlandsopphold(land(o.getLand()), new LukketPeriode(o.getTidsperiode().getFom(), o.getTidsperiode().getTom()));
    }

    private static CountryCode land(String land) {
        return isNullOrEmpty(land) ? null : CountryCode.valueOf(land);
    }
}
