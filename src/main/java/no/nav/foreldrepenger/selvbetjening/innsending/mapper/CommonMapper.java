package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import com.neovisionaries.i18n.CountryCode;
import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Orgnummer;
import no.nav.foreldrepenger.common.domain.felles.DokumentType;
import no.nav.foreldrepenger.common.domain.felles.InnsendingsType;
import no.nav.foreldrepenger.common.domain.felles.LukketPeriode;
import no.nav.foreldrepenger.common.domain.felles.ProsentAndel;
import no.nav.foreldrepenger.common.domain.felles.PåkrevdVedlegg;
import no.nav.foreldrepenger.common.domain.felles.VedleggMetaData;
import no.nav.foreldrepenger.common.domain.felles.annenforelder.AnnenForelder;
import no.nav.foreldrepenger.common.domain.felles.annenforelder.NorskForelder;
import no.nav.foreldrepenger.common.domain.felles.annenforelder.UkjentForelder;
import no.nav.foreldrepenger.common.domain.felles.annenforelder.UtenlandskForelder;
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
import no.nav.foreldrepenger.selvbetjening.innsending.domain.AnnenForelderFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.BarnFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.UtenlandsoppholdPeriodeFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.VedleggFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.AnnenInntektFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.FrilansInformasjonFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.FrilansoppdragFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.SelvstendigNæringsdrivendeInformasjonFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.TilknyttetPerson;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.now;
import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.selvbetjening.util.DateUtil.erNyopprettet;
import static org.springframework.util.ObjectUtils.isEmpty;

public final class CommonMapper {

    private CommonMapper() {
    }

    public static no.nav.foreldrepenger.common.domain.felles.Vedlegg tilVedlegg(VedleggFrontend vedlegg) {
        var vedleggMetadata = new VedleggMetaData(
            vedlegg.getId(),
            vedlegg.getInnsendingsType() != null ? InnsendingsType.valueOf(vedlegg.getInnsendingsType()) : null,
            vedlegg.getSkjemanummer() != null ? DokumentType.valueOf(vedlegg.getSkjemanummer()) : null,
            vedlegg.getBeskrivelse()
        );
        return new PåkrevdVedlegg(vedleggMetadata, vedlegg.getContent());
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
        if (Boolean.TRUE.equals(s.getErEndringssøknad())) {
            return null;
        }
        var opphold = s.getInformasjonOmUtenlandsopphold();
        return new Medlemsskap(
            tilUtenlandsoppholdsliste(opphold.tidligereOpphold()),
            tilUtenlandsoppholdsliste(opphold.senereOpphold()));
    }

    static Opptjening tilOpptjening(SøknadFrontend s) {
        if (Boolean.TRUE.equals(s.getErEndringssøknad())) {
            return null;
        }

        var søker = s.getSøker();
        return new Opptjening(
            tilUtenlandsArbeidsforhold(søker.andreInntekterSiste10Mnd()),
            tilEgenNæring(søker.selvstendigNæringsdrivendeInformasjon()),
            tilAnnenOpptjening(søker.andreInntekterSiste10Mnd()),
            tilFrilans(søker.frilansInformasjon())
        );
    }

    static RelasjonTilBarn tilRelasjonTilBarn(SøknadFrontend søknad) {
        var barn = søknad.getBarn();
        if (barn.adopsjonsdato() != null) {
            return tilAdopsjon(barn);
        }
        var situasjon = søknad.getSituasjon();
        if (isEmpty(situasjon) || situasjon.equals("fødsel")) {
            var erBarnetFødt = !isEmpty(barn.fødselsdatoer());
            return erBarnetFødt ? tilFødsel(barn) : tilFremtidigFødsel(barn);
        }
        return tilOmsorgsovertagelse(barn);
    }

    static Fødsel tilFødsel(BarnFrontend barn) {
        return new Fødsel(
            barn.antallBarn(),
            barn.fødselsdatoer(),
            barn.termindato(),
            barn.getAlleVedlegg()
        );
    }

    private static Omsorgsovertakelse tilOmsorgsovertagelse(BarnFrontend barn) {
        return new Omsorgsovertakelse(
            barn.antallBarn(),
            barn.foreldreansvarsdato(),
            barn.fødselsdatoer(),
            barn.getAlleVedlegg()
        );
    }

    private static FremtidigFødsel tilFremtidigFødsel(BarnFrontend barn) {
        return new FremtidigFødsel(
            barn.antallBarn(),
            barn.termindato(),
            barn.terminbekreftelseDato(),
            barn.getAlleVedlegg()
        );
    }

    private static Adopsjon tilAdopsjon(BarnFrontend barn) {
        return new Adopsjon(
            barn.antallBarn(),
            barn.adopsjonsdato(),
            barn.adopsjonAvEktefellesBarn(),
            barn.søkerAdopsjonAlene(),
            barn.getAlleVedlegg(),
            barn.ankomstdato(),
            barn.fødselsdatoer()
        );
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
        return annenForelder.fornavn() + " " + annenForelder.etternavn();
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
        return new UtenlandskArbeidsforhold(
            annenInntekt.arbeidsgiverNavn(),
            new ÅpenPeriode(annenInntekt.tidsperiode().fom(), annenInntekt.tidsperiode().tom()),
            annenInntekt.vedlegg(),
            land(annenInntekt.land())
        );
    }

    private static List<EgenNæring> tilEgenNæring(List<SelvstendigNæringsdrivendeInformasjonFrontend> selvstendigNæringsdrivendeInformasjon) {
        return selvstendigNæringsdrivendeInformasjon.stream()
            .map(CommonMapper::tilEgenNæring)
            .toList();
    }

    private static EgenNæring tilEgenNæring(SelvstendigNæringsdrivendeInformasjonFrontend selvstendig) {
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
            selvstendig.vedlegg()
        );
    }

    private static Orgnummer tilOrgnummer(SelvstendigNæringsdrivendeInformasjonFrontend selvstendig) {
        if (selvstendig.registrertINorge()) {
            return selvstendig.organisasjonsnummer() != null ? Orgnummer.valueOf(selvstendig.organisasjonsnummer()) : null;
        }
        return null;
    }

    private static Regnskapsfører tilRegnskapsfører(TilknyttetPerson person) {
        return new Regnskapsfører(person.navn(), person.telefonnummer());
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
            tilFrilansOppdrag(frilansInformasjon.oppdragForNæreVennerEllerFamilieSiste10Mnd()));
    }

    private static List<FrilansOppdrag> tilFrilansOppdrag(List<FrilansoppdragFrontend> oppdragForNæreVennerEllerFamilieSiste10Mnd) {
        return safeStream(oppdragForNæreVennerEllerFamilieSiste10Mnd)
            .map(CommonMapper::tilFrilansOppdrag)
            .toList();
    }

    private static FrilansOppdrag tilFrilansOppdrag(FrilansoppdragFrontend o) {
        return new FrilansOppdrag(o.navnPåArbeidsgiver(), new ÅpenPeriode(o.tidsperiode().fom(), o.tidsperiode().tom()));
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
        return isEmpty(land) ? CountryCode.UNDEFINED : CountryCode.valueOf(land);
    }

}
