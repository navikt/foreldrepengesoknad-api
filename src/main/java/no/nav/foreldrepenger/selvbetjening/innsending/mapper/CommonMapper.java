package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;
import static java.time.LocalDate.now;
import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.selvbetjening.util.DateUtil.erNyopprettet;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
import no.nav.foreldrepenger.selvbetjening.innsending.domain.AnnenForelderFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.BarnFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.UtenlandsoppholdPeriodeFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.AnnenInntektFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.FrilansInformasjonFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.FrilansoppdragFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.SelvstendigNæringsdrivendeInformasjonFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.TilknyttetPerson;

final class CommonMapper {

    private CommonMapper() {
    }


    static AnnenForelder tilAnnenForelder(SøknadFrontend søknad) {
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

    static Medlemsskap tilMedlemskap(SøknadFrontend s) {
        var opphold = s.getInformasjonOmUtenlandsopphold();
        var tidligereOppholdsInfo = tilTidligereOppholdsInfo(opphold.tidligereOpphold());
        var framtidigOppholdsInfo = tilFramtidigOppholdsinformasjon(opphold.senereOpphold());
        return new Medlemsskap(tidligereOppholdsInfo, framtidigOppholdsInfo);
    }

    static Opptjening tilOpptjening(SøknadFrontend s) {
        var søker = s.getSøker();
        return Opptjening.builder()
            .frilans(tilFrilans(søker.frilansInformasjon()))
            .egenNæring(tilEgenNæring(søker.selvstendigNæringsdrivendeInformasjon()))
            .utenlandskArbeidsforhold(tilUtenlandsArbeidsforhold(søker.andreInntekterSiste10Mnd()))
            .annenOpptjening(tilAnnenOpptjening(søker.andreInntekterSiste10Mnd()))
            .build();

    }

    static RelasjonTilBarn tilRelasjonTilBarn(SøknadFrontend søknad) {
        var barn = søknad.getBarn();
        if (barn.adopsjonsdato() != null) {
            return tilAdopsjon(barn);
        }
        var situasjon = søknad.getSituasjon();
        if (isEmpty(situasjon) || situasjon.equals("fødsel")) {
            var erBarnetFødt = !CollectionUtils.isEmpty(barn.fødselsdatoer());
            return erBarnetFødt ? tilFødsel(barn) : tilFremtidigFødsel(barn);
        }
        return tilOmsorgsovertagelse(barn);
    }

    static Fødsel tilFødsel(BarnFrontend barn) {
        return Fødsel.builder()
            .fødselsdato(barn.fødselsdatoer())
            .termindato(barn.termindato())
            .antallBarn(barn.antallBarn())
            .vedlegg(barn.getAlleVedlegg())
            .build();
    }

    private static Omsorgsovertakelse tilOmsorgsovertagelse(BarnFrontend barn) {
        return Omsorgsovertakelse.builder()
            .omsorgsovertakelsesdato(barn.foreldreansvarsdato())
            .årsak(null) // TODO: Ikke satt av api, heller ikke sendt ned fra frontend
            .fødselsdato(barn.fødselsdatoer())
            .antallBarn(barn.antallBarn())
            // .beskrivelse() ikke satt, ikke brukt?
            .vedlegg(barn.getAlleVedlegg())
            .build();
    }

    private static FremtidigFødsel tilFremtidigFødsel(BarnFrontend barn) {
        return FremtidigFødsel.builder()
            .terminDato(barn.termindato())
            .utstedtDato(barn.terminbekreftelseDato())
            .antallBarn(barn.antallBarn())
            .vedlegg(barn.getAlleVedlegg())
            .build();
    }

    private static Adopsjon tilAdopsjon(BarnFrontend barn) {
        return Adopsjon.builder()
            .omsorgsovertakelsesdato(barn.adopsjonsdato())
            .ektefellesBarn(barn.adopsjonAvEktefellesBarn())
            .søkerAdopsjonAlene(barn.søkerAdopsjonAlene())
            .ankomstDato(barn.ankomstdato())
            .fødselsdato(barn.fødselsdatoer())
            .antallBarn(barn.antallBarn())
            .vedlegg(barn.getAlleVedlegg())
            .build();
    }

    private static UtenlandskForelder tilUtenlandskForelder(AnnenForelderFrontend annenForelder) {
        return new UtenlandskForelder(
            annenForelder.fnr(),
            land(annenForelder.bostedsland()),
            navn(annenForelder)
        );
    }


    private static NorskForelder tilNorskForelder(AnnenForelderFrontend annenForelder) {
        return new NorskForelder(new Fødselsnummer(annenForelder.fnr()), navn(annenForelder));
    }

    private static String navn(AnnenForelderFrontend annenForelder) {
        return isNotBlank(annenForelder.navn()) ? annenForelder.navn() :
            annenForelder.fornavn() + " " + annenForelder.etternavn();
    }

    private static List<AnnenOpptjening> tilAnnenOpptjening(List<AnnenInntektFrontend> andreInntekterSiste10Mnd) {
        return andreInntekterSiste10Mnd.stream()
            .filter(annenInntekt -> !annenInntekt.type().equals("JOBB_I_UTLANDET"))
            .map(CommonMapper::tilAnnenOpptjening)
            .toList();
    }

    private static AnnenOpptjening tilAnnenOpptjening(AnnenInntektFrontend annenInntekt) {
        return new AnnenOpptjening(
            annenInntekt.type() != null ? AnnenOpptjeningType.valueOf(annenInntekt.type()) : null,
            new ÅpenPeriode(annenInntekt.tidsperiode().fom(), annenInntekt.tidsperiode().tom()),
            annenInntekt.vedlegg());
    }

    private static List<UtenlandskArbeidsforhold> tilUtenlandsArbeidsforhold(List<AnnenInntektFrontend> andreInntekterSiste10Mnd) {
        return andreInntekterSiste10Mnd.stream()
            .filter(annenInntekt -> annenInntekt.type().equals("JOBB_I_UTLANDET"))
            .map(CommonMapper::tilUtenlandsArbeidsforhold)
            .toList();

    }

    private static UtenlandskArbeidsforhold tilUtenlandsArbeidsforhold(AnnenInntektFrontend annenInntekt) {
        return UtenlandskArbeidsforhold.builder()
            .arbeidsgiverNavn(annenInntekt.arbeidsgiverNavn())
            .land(land(annenInntekt.land()))
            .periode(new ÅpenPeriode(annenInntekt.tidsperiode().fom(), annenInntekt.tidsperiode().tom()))
            .vedlegg(annenInntekt.vedlegg())
            .build();
    }

    private static List<EgenNæring> tilEgenNæring(List<SelvstendigNæringsdrivendeInformasjonFrontend> selvstendigNæringsdrivendeInformasjon) {
        return selvstendigNæringsdrivendeInformasjon.stream()
            .map(CommonMapper::tilEgenNæring)
            .toList();
    }

    private static EgenNæring tilEgenNæring(SelvstendigNæringsdrivendeInformasjonFrontend selvstendig) {
        return selvstendig.registrertINorge() ? tilNorskNæring(selvstendig) : tilUtenlandskOrganisasjon(selvstendig);
    }

    private static NorskOrganisasjon tilNorskNæring(SelvstendigNæringsdrivendeInformasjonFrontend selvstendig) {
        // Spesifikk
        var norskOrganisasjonBuilder = NorskOrganisasjon.builder()
            .orgName(selvstendig.navnPåNæringen())
            .orgNummer(selvstendig.organisasjonsnummer() != null ? Orgnummer.valueOf(selvstendig.organisasjonsnummer()): null);


        // Generelle TODO: Identisk til UtenlandskOrgansiasjon.. Mye duplisering... skrive bort i fra abstract class maybe?
        norskOrganisasjonBuilder
            .stillingsprosent(selvstendig.stillingsprosent() != null ? new ProsentAndel(selvstendig.stillingsprosent()) : null)
            .periode(new ÅpenPeriode(selvstendig.tidsperiode().fom(), selvstendig.tidsperiode().tom()))
            .erNyIArbeidslivet(selvstendig.harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene())
            .erNyOpprettet(erNyopprettet(selvstendig.tidsperiode().fom()))
            .erVarigEndring(selvstendig.hattVarigEndringAvNæringsinntektSiste4Kalenderår())
            .vedlegg(selvstendig.vedlegg())
            .virksomhetsTyper(tilVirksomhetsTyper(selvstendig.næringstyper()))
            .oppstartsDato(selvstendig.oppstartsdato());

        var næringsInfo = selvstendig.endringAvNæringsinntektInformasjon();
        if (næringsInfo != null) {
            norskOrganisasjonBuilder
                .endringsDato(næringsInfo.dato())
                .næringsinntektBrutto(næringsInfo.næringsinntektEtterEndring())
                .beskrivelseEndring(næringsInfo.forklaring());
        } else {
            norskOrganisasjonBuilder.næringsinntektBrutto(selvstendig.næringsinntekt());
        }

        var regnskapsfører = selvstendig.regnskapsfører();
        var revisor = selvstendig.revisor();
        if (regnskapsfører != null) {
            norskOrganisasjonBuilder
                .regnskapsførere(List.of(tilRegnskapsfører(regnskapsfører)))
                .nærRelasjon(regnskapsfører.erNærVennEllerFamilie());
        } else if (revisor != null) {
            norskOrganisasjonBuilder
                .regnskapsførere(List.of(tilRegnskapsfører(revisor)))
                .nærRelasjon(revisor.erNærVennEllerFamilie());
        }

        return norskOrganisasjonBuilder.build();
    }

    private static UtenlandskOrganisasjon tilUtenlandskOrganisasjon(SelvstendigNæringsdrivendeInformasjonFrontend selvstendig) {
        // Spesifikk
        var utenlandskOrganisasjonBuilder = UtenlandskOrganisasjon.builder()
            .orgName(selvstendig.navnPåNæringen())
            .registrertILand(CountryCode.valueOf(selvstendig.registrertILand()));

        // Generelle
        utenlandskOrganisasjonBuilder
            .stillingsprosent(selvstendig.stillingsprosent() != null ? new ProsentAndel(selvstendig.stillingsprosent()) : null)
            .periode(new ÅpenPeriode(selvstendig.tidsperiode().fom(), selvstendig.tidsperiode().tom()))
            .erNyIArbeidslivet(selvstendig.harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene())
            .erNyOpprettet(erNyopprettet(selvstendig.tidsperiode().fom()))
            .erVarigEndring(selvstendig.hattVarigEndringAvNæringsinntektSiste4Kalenderår())
            .vedlegg(selvstendig.vedlegg())
            .virksomhetsTyper(tilVirksomhetsTyper(selvstendig.næringstyper()))
            .oppstartsDato(selvstendig.oppstartsdato());

        var næringsInfo = selvstendig.endringAvNæringsinntektInformasjon();
        if (næringsInfo != null) {
            utenlandskOrganisasjonBuilder
                .endringsDato(næringsInfo.dato())
                .næringsinntektBrutto(næringsInfo.næringsinntektEtterEndring())
                .beskrivelseEndring(næringsInfo.forklaring());
        } else {
            utenlandskOrganisasjonBuilder.næringsinntektBrutto(selvstendig.næringsinntekt());
        }

        var regnskapsfører = selvstendig.regnskapsfører();
        var revisor = selvstendig.revisor();
        if (regnskapsfører != null) {
            utenlandskOrganisasjonBuilder
                .regnskapsførere(List.of(tilRegnskapsfører(regnskapsfører)))
                .nærRelasjon(regnskapsfører.erNærVennEllerFamilie());
        } else if (revisor != null) {
            utenlandskOrganisasjonBuilder
                .regnskapsførere(List.of(tilRegnskapsfører(revisor)))
                .nærRelasjon(revisor.erNærVennEllerFamilie());
        }

        return utenlandskOrganisasjonBuilder.build();
    }

    private static Regnskapsfører tilRegnskapsfører(TilknyttetPerson person) {
        return new Regnskapsfører(person.navn(), person.telefonnummer());
    }

    private static List<Virksomhetstype> tilVirksomhetsTyper(List<String> næringstyper) {
        return safeStream(næringstyper)
            .map(Virksomhetstype::valueOf)
            .toList();
    }

    private static Frilans tilFrilans(FrilansInformasjonFrontend frilansInformasjon) {
        if (frilansInformasjon == null)  {
            return null;
        }
        return new Frilans(
            new ÅpenPeriode(frilansInformasjon.oppstart()),
            frilansInformasjon.driverFosterhjem(),
            frilansInformasjon.oppstart().isAfter(now().minusMonths(3)),
            frilansInformasjon.jobberFremdelesSomFrilans(),
            tilFrilansOppdrag(frilansInformasjon.oppdragForNæreVennerEllerFamilieSiste10Mnd()),
            null); // TODO: Fjern denne som ikke blir brukt
    }

    private static List<FrilansOppdrag> tilFrilansOppdrag(List<FrilansoppdragFrontend> oppdragForNæreVennerEllerFamilieSiste10Mnd) {
        return safeStream(oppdragForNæreVennerEllerFamilieSiste10Mnd)
            .map(CommonMapper::tilFrilansOppdrag)
            .toList();
    }

    private static FrilansOppdrag tilFrilansOppdrag(FrilansoppdragFrontend o) {
        return new FrilansOppdrag(o.navnPåArbeidsgiver(), new ÅpenPeriode(o.tidsperiode().fom(), o.tidsperiode().tom()));
    }

    private static FramtidigOppholdsInformasjon tilFramtidigOppholdsinformasjon(List<UtenlandsoppholdPeriodeFrontend> senereOpphold) {
        return new FramtidigOppholdsInformasjon(tilUtenlandsoppholdsliste(senereOpphold));
    }

    private static TidligereOppholdsInformasjon tilTidligereOppholdsInfo(List<UtenlandsoppholdPeriodeFrontend> tidligereOpphold) {
        return new TidligereOppholdsInformasjon(ArbeidsInformasjon.IKKE_ARBEIDET, tilUtenlandsoppholdsliste(tidligereOpphold));
    }

    private static List<Utenlandsopphold> tilUtenlandsoppholdsliste(List<UtenlandsoppholdPeriodeFrontend> tidligereOpphold) {
        return safeStream(tidligereOpphold)
            .map(CommonMapper::tilUtenlandsopphold)
            .toList();
    }

    private static Utenlandsopphold tilUtenlandsopphold(UtenlandsoppholdPeriodeFrontend o) {
        return new Utenlandsopphold(land(o.land()), new LukketPeriode(o.tidsperiode().fom(), o.tidsperiode().tom()));
    }

    public static CountryCode land(String land) {
        return isNullOrEmpty(land) ? CountryCode.UNDEFINED : CountryCode.valueOf(land);
    }

}
