package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FELLESPERIODE;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FORELDREPENGER_FØR_FØDSEL;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.MØDREKVOTE;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.UttakplanPeriodeBuilder.gradert;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.UttakplanPeriodeBuilder.utsettelse;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.UttakplanPeriodeBuilder.uttak;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler.MedlemsskapMaler.medlemskapUtlandetForrige12mnd;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler.MedlemsskapMaler.medlemsskapNorge;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.felles.VedleggReferanse;
import no.nav.foreldrepenger.common.domain.felles.annenforelder.NorskForelder;
import no.nav.foreldrepenger.common.domain.felles.annenforelder.UkjentForelder;
import no.nav.foreldrepenger.common.domain.felles.opptjening.AnnenOpptjening;
import no.nav.foreldrepenger.common.domain.felles.opptjening.AnnenOpptjeningType;
import no.nav.foreldrepenger.common.domain.felles.opptjening.UtenlandskArbeidsforhold;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Foreldrepenger;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.GradertUttaksPeriode;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.LukketPeriodeMedVedlegg;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UtsettelsesÅrsak;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UttaksPeriode;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.Dekningsgrad;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ÅpenPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.AnnenforelderBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.BarnBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.ForeldrepengerBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.SøkerBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler.OpptjeningMaler;

class ForeldrepengesoknadMappingKonsistensTest {
    private static final LocalDate NOW = LocalDate.now();
    private static final Fødselsnummer DUMMY_FNR = new Fødselsnummer("0000000000");


    @Test
    void fpGraderingFrilansAleneomsorgFarIkkeRettMappingKonsistensTest() {
        var uttak = List.of(uttak(FORELDREPENGER_FØR_FØDSEL, NOW.minusWeeks(3), NOW.minusDays(1)).build(),
            uttak(MØDREKVOTE, NOW, NOW.plusWeeks(15).minusDays(1)).build(),
            gradert(FELLESPERIODE, NOW.plusWeeks(15), NOW.plusWeeks(45).minusDays(1), 33.3).build());
        var søknadDto = new ForeldrepengerBuilder().medFordeling(uttak)
            .medDekningsgrad(Dekningsgrad.HUNDRE)
            .medMedlemsskap(medlemsskapNorge())
            .medSøker(new SøkerBuilder(BrukerRolle.MOR).medErAleneOmOmsorg(true).medFrilansInformasjon(OpptjeningMaler.frilansOpptjening()).build())
            .medAnnenForelder(AnnenforelderBuilder.norskIkkeRett(DUMMY_FNR).build())
            .medBarn(BarnBuilder.termin(2, LocalDate.now().minusWeeks(2)).build())
            .build();
        var foreldrepengerDto = ((ForeldrepengesøknadDto) søknadDto);

        var mappedSøknad = SøknadMapper.tilSøknad(søknadDto, NOW);
        assertThat(mappedSøknad.getSøker().søknadsRolle()).isEqualTo(søknadDto.søker().rolle());
        assertThat(mappedSøknad.getSøker().målform()).isEqualTo(søknadDto.søker().språkkode());
        assertThat(mappedSøknad.getMottattdato()).isEqualTo(søknadDto.mottattdato());
        assertThat(mappedSøknad.getTilleggsopplysninger()).isEqualTo(foreldrepengerDto.tilleggsopplysninger());

        var ytelse = mappedSøknad.getYtelse();
        assertThat(ytelse).isInstanceOf(Foreldrepenger.class);
        var foreldrepenger = (Foreldrepenger) ytelse;
        assertThat(foreldrepenger.dekningsgrad()).isEqualTo(no.nav.foreldrepenger.common.domain.foreldrepenger.Dekningsgrad.HUNDRE);
        assertThat(foreldrepenger.utenlandsopphold().opphold()).isEmpty();

        // Annenpart
        assertThat(foreldrepenger.annenForelder()).isInstanceOf(NorskForelder.class);
        var norskforelder = (NorskForelder) foreldrepenger.annenForelder();
        assertThat(norskforelder.fnr().value()).isEqualTo(foreldrepengerDto.annenForelder().fnr());

        // Frilans
        var frilans = foreldrepenger.opptjening().frilans();
        var frilansDto = foreldrepengerDto.søker().frilansInformasjon();
        assertThat(frilans.periode().fom()).isEqualTo(frilansDto.oppstart());
        assertThat(frilans.jobberFremdelesSomFrilans()).isEqualTo(frilansDto.jobberFremdelesSomFrilans());

        // Rettigheter
        var rettigheter = foreldrepenger.rettigheter();
        assertThat(rettigheter.harAleneOmsorgForBarnet()).isEqualTo(foreldrepengerDto.søker().erAleneOmOmsorg());
        assertThat(rettigheter.harAnnenForelderRett()).isEqualTo(foreldrepengerDto.annenForelder().harRettPåForeldrepenger());
        assertThat(rettigheter.harMorUføretrygd()).isEqualTo(foreldrepengerDto.annenForelder().harMorUføretrygd());
        assertThat(rettigheter.harAnnenForelderOppholdtSegIEØS()).isEqualTo(foreldrepengerDto.annenForelder().harAnnenForelderOppholdtSegIEØS());
        assertThat(rettigheter.harAnnenForelderTilsvarendeRettEØS()).isEqualTo(
            foreldrepengerDto.annenForelder().harAnnenForelderTilsvarendeRettEØS());

        // Fordeling
        assertThat(foreldrepenger.fordeling().ønskerJustertUttakVedFødsel()).isEqualTo(foreldrepengerDto.ønskerJustertUttakVedFødsel());
        assertThat(foreldrepenger.fordeling().perioder()).hasSameSizeAs(foreldrepengerDto.uttaksplan())
            .hasExactlyElementsOfTypes(UttaksPeriode.class, UttaksPeriode.class, GradertUttaksPeriode.class)
            .extracting(LukketPeriodeMedVedlegg::getFom)
            .containsExactly(uttak.get(0).tidsperiode().fom(), uttak.get(1).tidsperiode().fom(), uttak.get(2).tidsperiode().fom());
        assertThat(foreldrepenger.fordeling().perioder()).extracting(LukketPeriodeMedVedlegg::getTom)
            .containsExactly(uttak.get(0).tidsperiode().tom(), uttak.get(1).tidsperiode().tom(), uttak.get(2).tidsperiode().tom());

    }

    @Test
    void fpAdopsjonAnnenInntektMappingKonsistensTest() {
        var uttak = List.of(uttak(FORELDREPENGER_FØR_FØDSEL, NOW.minusWeeks(3), NOW.minusDays(1)).build(),
            uttak(MØDREKVOTE, NOW, NOW.plusWeeks(15).minusDays(1)).build(),
            uttak(FELLESPERIODE, NOW.plusWeeks(15), NOW.plusWeeks(31).minusDays(1)).build());
        var utenlandskOpptjening = OpptjeningMaler.utenlandskArbeidsforhold(CountryCode.US);
        var annenNorskOpptjening = OpptjeningMaler.annenInntektNorsk(AnnenOpptjeningType.SLUTTPAKKE);
        var annenOpptjeninger = List.of(utenlandskOpptjening, annenNorskOpptjening);
        var søknadDto = new ForeldrepengerBuilder().medFordeling(uttak)
            .medØnskerJustertUttakVedFødsel(true)
            .medDekningsgrad(Dekningsgrad.ÅTTI)
            .medMedlemsskap(medlemskapUtlandetForrige12mnd())
            .medSøker(new SøkerBuilder(BrukerRolle.MOR).medAndreInntekterSiste10Mnd(annenOpptjeninger).build())
            .medAnnenForelder(AnnenforelderBuilder.ukjentForelder())
            .medBarn(BarnBuilder.adopsjon(LocalDate.now().minusWeeks(2), false).build())
            .build();
        var foreldrepengerDto = ((ForeldrepengesøknadDto) søknadDto);

        var mappedSøknad = SøknadMapper.tilSøknad(søknadDto, NOW);
        assertThat(mappedSøknad.getSøker().søknadsRolle()).isEqualTo(søknadDto.søker().rolle());
        assertThat(mappedSøknad.getSøker().målform()).isEqualTo(søknadDto.søker().språkkode());
        assertThat(mappedSøknad.getMottattdato()).isEqualTo(søknadDto.mottattdato());
        assertThat(mappedSøknad.getTilleggsopplysninger()).isEqualTo(foreldrepengerDto.tilleggsopplysninger());

        var ytelse = mappedSøknad.getYtelse();
        assertThat(ytelse).isInstanceOf(Foreldrepenger.class);
        var foreldrepenger = (Foreldrepenger) ytelse;
        assertThat(foreldrepenger.dekningsgrad()).isEqualTo(no.nav.foreldrepenger.common.domain.foreldrepenger.Dekningsgrad.ÅTTI);

        // Annenpart
        assertThat(foreldrepenger.annenForelder()).isInstanceOf(UkjentForelder.class);

        // Medlemsskap
        assertThat(foreldrepenger.utenlandsopphold().landVedDato(LocalDate.now())).isEqualByComparingTo(CountryCode.US);
        assertThat(foreldrepenger.utenlandsopphold().landVedDato(LocalDate.now().plusMonths(1))).isEqualByComparingTo(CountryCode.NO);
        assertThat(foreldrepenger.utenlandsopphold().opphold()).hasSize(1);

        // AnnenOpptjening
        assertThat(foreldrepenger.opptjening().annenOpptjening()).hasSize(1)
            .extracting(AnnenOpptjening::type)
            .containsExactly(AnnenOpptjeningType.valueOf(annenNorskOpptjening.type()));
        assertThat(foreldrepenger.opptjening().utenlandskArbeidsforhold()).hasSize(1)
            .extracting(UtenlandskArbeidsforhold::land)
            .containsExactly(CountryCode.valueOf(utenlandskOpptjening.land()));

        // Rettigheter
        var rettigheter = foreldrepenger.rettigheter();
        assertThat(rettigheter.harAleneOmsorgForBarnet()).isEqualTo(foreldrepengerDto.søker().erAleneOmOmsorg());
        assertThat(rettigheter.harAnnenForelderRett()).isEqualTo(foreldrepengerDto.annenForelder().harRettPåForeldrepenger());
        assertThat(rettigheter.harMorUføretrygd()).isEqualTo(foreldrepengerDto.annenForelder().harMorUføretrygd());
        assertThat(rettigheter.harAnnenForelderOppholdtSegIEØS()).isEqualTo(foreldrepengerDto.annenForelder().harAnnenForelderOppholdtSegIEØS());
        assertThat(rettigheter.harAnnenForelderTilsvarendeRettEØS()).isEqualTo(
            foreldrepengerDto.annenForelder().harAnnenForelderTilsvarendeRettEØS());

        // Fordeling
        assertThat(foreldrepenger.fordeling().ønskerJustertUttakVedFødsel()).isEqualTo(foreldrepengerDto.ønskerJustertUttakVedFødsel());
        assertThat(foreldrepenger.fordeling().perioder()).hasSameSizeAs(foreldrepengerDto.uttaksplan())
            .hasExactlyElementsOfTypes(UttaksPeriode.class, UttaksPeriode.class, UttaksPeriode.class)
            .extracting(LukketPeriodeMedVedlegg::getFom)
            .containsExactly(uttak.get(0).tidsperiode().fom(), uttak.get(1).tidsperiode().fom(), uttak.get(2).tidsperiode().fom());
        assertThat(foreldrepenger.fordeling().perioder()).extracting(LukketPeriodeMedVedlegg::getTom)
            .containsExactly(uttak.get(0).tidsperiode().tom(), uttak.get(1).tidsperiode().tom(), uttak.get(2).tidsperiode().tom());
    }

    @Test
    void foreldrepengerVedleggReferanseMappingKonsistensTest() {
        var uttak = List.of(uttak(FORELDREPENGER_FØR_FØDSEL, NOW.minusWeeks(3), NOW.minusDays(1)).build(),
            utsettelse(UtsettelsesÅrsak.SYKDOM, NOW, NOW.plusWeeks(1).minusDays(1)).build(),
            utsettelse(UtsettelsesÅrsak.INSTITUSJONSOPPHOLD_SØKER, NOW.plusWeeks(1), NOW.plusWeeks(3).minusDays(1)).build(),
            uttak(MØDREKVOTE, NOW.plusWeeks(3), NOW.plusWeeks(4).minusDays(1)).build(),
            utsettelse(UtsettelsesÅrsak.SYKDOM, NOW.plusWeeks(4), NOW.plusWeeks(6).minusDays(1)).build(),
            uttak(MØDREKVOTE, NOW.plusWeeks(6), NOW.plusWeeks(15).minusDays(1)).build());
        var utenlandskOpptjening = OpptjeningMaler.utenlandskArbeidsforhold(CountryCode.US);
        var annenNorskOpptjening = OpptjeningMaler.annenInntektNorsk(AnnenOpptjeningType.SLUTTPAKKE,
            new ÅpenPeriodeDto(LocalDate.now().minusYears(1), LocalDate.now()));

        var vedlegg1 = DokumentasjonUtil.vedlegg(DokumentasjonUtil.barn());
        var vedlegg2 = DokumentasjonUtil.vedlegg(DokumentasjonUtil.uttaksperioder(
            List.of(new ÅpenPeriodeDto(NOW, NOW.plusWeeks(1).minusDays(1)), new ÅpenPeriodeDto(NOW.plusWeeks(4), NOW.plusWeeks(6).minusDays(1)))));
        var vedlegg3 = DokumentasjonUtil.vedlegg(DokumentasjonUtil.uttaksperiode(NOW.plusWeeks(1), NOW.plusWeeks(3).minusDays(1)));
        var vedlegg4 = DokumentasjonUtil.vedlegg(DokumentasjonUtil.opptjening(utenlandskOpptjening.tidsperiode()));
        var vedlegg5 = DokumentasjonUtil.vedlegg(DokumentasjonUtil.opptjening(annenNorskOpptjening.tidsperiode()));

        var søknadDto = new ForeldrepengerBuilder().medFordeling(uttak)
            .medØnskerJustertUttakVedFødsel(true)
            .medDekningsgrad(Dekningsgrad.ÅTTI)
            .medMedlemsskap(medlemskapUtlandetForrige12mnd())
            .medSøker(new SøkerBuilder(BrukerRolle.MOR).medAndreInntekterSiste10Mnd(List.of(utenlandskOpptjening, annenNorskOpptjening)).build())
            .medAnnenForelder(AnnenforelderBuilder.ukjentForelder())
            .medBarn(BarnBuilder.adopsjon(LocalDate.now().minusWeeks(2), false).build())
            .medVedlegg(List.of(vedlegg1, vedlegg2, vedlegg3, vedlegg4, vedlegg5))
            .build();
        var mappedSøknad = SøknadMapper.tilSøknad(søknadDto, NOW);
        var foreldrepenger = (Foreldrepenger) mappedSøknad.getYtelse();

        assertThat(mappedSøknad.getVedlegg()).hasSameSizeAs(søknadDto.vedlegg());
        assertThat(vedlegg1.referanse().verdi()).isNotNull();
        assertThat(foreldrepenger.relasjonTilBarn().getVedlegg()).extracting(VedleggReferanse::referanse)
            .containsExactly(vedlegg1.referanse().verdi());

        assertThat(foreldrepenger.fordeling().perioder().get(0).getVedlegg()).isEmpty();
        assertThat(foreldrepenger.fordeling().perioder().get(1).getVedlegg()).extracting(VedleggReferanse::referanse)
            .containsExactly(vedlegg2.referanse().verdi());
        assertThat(foreldrepenger.fordeling().perioder().get(2).getVedlegg()).extracting(VedleggReferanse::referanse)
            .containsExactly(vedlegg3.referanse().verdi());
        assertThat(foreldrepenger.fordeling().perioder().get(3).getVedlegg()).isEmpty();
        assertThat(foreldrepenger.fordeling().perioder().get(4).getVedlegg()).extracting(VedleggReferanse::referanse)
            .containsExactly(vedlegg2.referanse().verdi());
        assertThat(foreldrepenger.fordeling().perioder().get(5).getVedlegg()).isEmpty();

        assertThat(foreldrepenger.opptjening().utenlandskArbeidsforhold().getFirst().vedlegg()).extracting(VedleggReferanse::referanse)
            .containsExactly(vedlegg4.referanse().verdi());
        assertThat(foreldrepenger.opptjening().annenOpptjening().getFirst().vedlegg()).extracting(VedleggReferanse::referanse)
            .containsExactly(vedlegg5.referanse().verdi());
    }
}
