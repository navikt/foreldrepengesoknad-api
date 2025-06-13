package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper;

import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FELLESPERIODE;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FORELDREPENGER_FØR_FØDSEL;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.MØDREKVOTE;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.UttakplanPeriodeBuilder.gradert;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.UttakplanPeriodeBuilder.utsettelse;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.UttakplanPeriodeBuilder.uttak;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.maler.UtenlandsoppholdMaler.oppholdBareINorge;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.maler.UtenlandsoppholdMaler.oppholdIUtlandetForrige12mnd;
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
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ÅpenPeriodeDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.DokumentasjonUtil;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.SøknadMapper;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.AnnenInntektDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.Dekningsgrad;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.UttaksplanDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.AnnenforelderBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.BarnBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.ForeldrepengerBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.maler.OpptjeningMaler;

class ForeldrepengesoknadMappingKonsistensTest {
    private static final LocalDate NOW = LocalDate.now();
    private static final Fødselsnummer DUMMY_FNR = new Fødselsnummer("0000000000");


    @Test
    void fpGraderingFrilansAleneomsorgFarIkkeRettMappingKonsistensTest() {
        var uttak = List.of(uttak(FORELDREPENGER_FØR_FØDSEL, NOW.minusWeeks(3), NOW.minusDays(1)).build(),
            uttak(MØDREKVOTE, NOW, NOW.plusWeeks(15).minusDays(1)).build(),
            gradert(FELLESPERIODE, NOW.plusWeeks(15), NOW.plusWeeks(45).minusDays(1), 33.3).build());
        var søknadDto = new ForeldrepengerBuilder().medRolle(BrukerRolle.MOR)
            .medUttaksplan(uttak)
            .medDekningsgrad(Dekningsgrad.HUNDRE)
            .medUtenlandsopphold(oppholdBareINorge())
            .medFrilansInformasjon(OpptjeningMaler.frilansOpptjening())
            .medAnnenForelder(AnnenforelderBuilder.norskIkkeRett(DUMMY_FNR).build())
            .medBarn(BarnBuilder.termin(2, LocalDate.now().minusWeeks(2)).build())
            .build();
        var foreldrepengerDto = ((ForeldrepengesøknadDto) søknadDto);

        var mappedSøknad = SøknadMapper.tilSøknad(søknadDto, NOW);
        assertThat(mappedSøknad.getSøker().søknadsRolle()).isEqualTo(foreldrepengerDto.rolle());
        assertThat(mappedSøknad.getSøker().målform()).isEqualTo(foreldrepengerDto.språkkode());
        assertThat(mappedSøknad.getMottattdato()).isEqualTo(søknadDto.mottattdato());

        var ytelse = mappedSøknad.getYtelse();
        assertThat(ytelse).isInstanceOf(Foreldrepenger.class);
        var foreldrepenger = (Foreldrepenger) ytelse;
        assertThat(foreldrepenger.dekningsgrad()).isEqualTo(no.nav.foreldrepenger.common.domain.foreldrepenger.Dekningsgrad.HUNDRE);
        assertThat(foreldrepenger.utenlandsopphold().landVedDato(LocalDate.now().minusMonths(2))).isEqualByComparingTo(CountryCode.NO);
        assertThat(foreldrepenger.utenlandsopphold().landVedDato(LocalDate.now().plusMonths(2))).isEqualByComparingTo(CountryCode.NO);
        assertThat(foreldrepenger.utenlandsopphold().opphold()).isEmpty();

        // Annenpart
        assertThat(foreldrepenger.annenForelder()).isInstanceOf(NorskForelder.class);
        var norskforelder = (NorskForelder) foreldrepenger.annenForelder();
        var annenForelderDto = foreldrepengerDto.annenForelder();
        assertThat(norskforelder.fnr()).isEqualTo(annenForelderDto.fnr());

        // Frilans
        var frilans = foreldrepenger.opptjening().frilans();
        var frilansDto = foreldrepengerDto.frilans();
        assertThat(frilans.periode().fom()).isEqualTo(frilansDto.oppstart());
        assertThat(frilans.jobberFremdelesSomFrilans()).isEqualTo(frilansDto.jobberFremdelesSomFrilans());

        // Rettigheter
        var rettigheter = foreldrepenger.rettigheter();
        assertThat(rettigheter.harAleneOmsorgForBarnet()).isEqualTo(foreldrepengerDto.annenForelder().rettigheter().erAleneOmOmsorg());
        assertThat(rettigheter.harAnnenForelderRett()).isEqualTo(annenForelderDto.rettigheter().harRettPåForeldrepenger());
        assertThat(rettigheter.harMorUføretrygd()).isEqualTo(annenForelderDto.rettigheter().harMorUføretrygd());
        assertThat(rettigheter.harAnnenForelderOppholdtSegIEØS()).isEqualTo(annenForelderDto.rettigheter().harAnnenForelderOppholdtSegIEØS());
        assertThat(rettigheter.harAnnenForelderTilsvarendeRettEØS()).isEqualTo(annenForelderDto.rettigheter().harAnnenForelderTilsvarendeRettEØS());

        // Fordeling
        assertThat(foreldrepenger.fordeling().ønskerJustertUttakVedFødsel()).isEqualTo(foreldrepengerDto.uttaksplan().ønskerJustertUttakVedFødsel());
        assertThat(foreldrepenger.fordeling().perioder()).hasSameSizeAs(foreldrepengerDto.uttaksplan().uttaksperioder())
            .hasExactlyElementsOfTypes(UttaksPeriode.class, UttaksPeriode.class, GradertUttaksPeriode.class)
            .extracting(LukketPeriodeMedVedlegg::getFom)
            .containsExactly(uttak.get(0).fom(), uttak.get(1).fom(), uttak.get(2).fom());
        assertThat(foreldrepenger.fordeling().perioder()).extracting(LukketPeriodeMedVedlegg::getTom)
            .containsExactly(uttak.get(0).tom(), uttak.get(1).tom(), uttak.get(2).tom());

    }

    @Test
    void fpAdopsjonAnnenInntektMappingKonsistensTest() {
        var uttak = List.of(uttak(FORELDREPENGER_FØR_FØDSEL, NOW.minusWeeks(3), NOW.minusDays(1)).build(),
            uttak(MØDREKVOTE, NOW, NOW.plusWeeks(15).minusDays(1)).build(),
            uttak(FELLESPERIODE, NOW.plusWeeks(15), NOW.plusWeeks(31).minusDays(1)).build());
        var utenlandskOpptjening = OpptjeningMaler.utenlandskArbeidsforhold(CountryCode.US);
        var annenNorskOpptjening = OpptjeningMaler.annenInntektNorsk(AnnenOpptjeningType.SLUTTPAKKE);
        var annenOpptjeninger = List.of(utenlandskOpptjening, annenNorskOpptjening);
        var søknadDto = new ForeldrepengerBuilder().medRolle(BrukerRolle.MOR)
            .medUttaksplan(new UttaksplanDto(true, uttak))
            .medDekningsgrad(Dekningsgrad.ÅTTI)
            .medUtenlandsopphold(oppholdIUtlandetForrige12mnd())
            .medAndreInntekterSiste10Mnd(annenOpptjeninger)
            .medAnnenForelder(AnnenforelderBuilder.ukjentForelder())
            .medBarn(BarnBuilder.adopsjon(LocalDate.now().minusWeeks(2), false).build())
            .build();
        var foreldrepengerDto = ((ForeldrepengesøknadDto) søknadDto);

        var mappedSøknad = SøknadMapper.tilSøknad(søknadDto, NOW);
        assertThat(mappedSøknad.getSøker().søknadsRolle()).isEqualTo(foreldrepengerDto.rolle());
        assertThat(mappedSøknad.getSøker().målform()).isEqualTo(foreldrepengerDto.språkkode());
        assertThat(mappedSøknad.getMottattdato()).isEqualTo(søknadDto.mottattdato());

        var ytelse = mappedSøknad.getYtelse();
        assertThat(ytelse).isInstanceOf(Foreldrepenger.class);
        var foreldrepenger = (Foreldrepenger) ytelse;
        assertThat(foreldrepenger.dekningsgrad()).isEqualTo(no.nav.foreldrepenger.common.domain.foreldrepenger.Dekningsgrad.ÅTTI);

        // Annenpart
        assertThat(foreldrepenger.annenForelder()).isInstanceOf(UkjentForelder.class);

        // Medlemsskap
        assertThat(foreldrepenger.utenlandsopphold().landVedDato(LocalDate.now().minusMonths(2))).isNotEqualByComparingTo(CountryCode.NO);
        assertThat(foreldrepenger.utenlandsopphold().landVedDato(LocalDate.now().plusMonths(2))).isEqualByComparingTo(CountryCode.NO);
        assertThat(foreldrepenger.utenlandsopphold().opphold()).hasSize(1);

        // AnnenOpptjening
        assertThat(foreldrepenger.opptjening().annenOpptjening()).hasSize(1)
            .extracting(AnnenOpptjening::type)
            .containsExactly(AnnenOpptjeningType.SLUTTPAKKE);
        assertThat(foreldrepenger.opptjening().utenlandskArbeidsforhold()).hasSize(1)
            .extracting(UtenlandskArbeidsforhold::land)
            .containsExactly(((AnnenInntektDto.Utlandet) utenlandskOpptjening).land());

        // Rettigheter
        var rettigheter = foreldrepenger.rettigheter();
        assertThat(rettigheter.harAnnenForelderRett()).isFalse();
        assertThat(rettigheter.harAleneOmsorgForBarnet()).isNull();
        assertThat(rettigheter.harMorUføretrygd()).isNull();
        assertThat(rettigheter.harAnnenForelderOppholdtSegIEØS()).isNull();
        assertThat(rettigheter.harAnnenForelderTilsvarendeRettEØS()).isNull();

        // Fordeling
        assertThat(foreldrepenger.fordeling().ønskerJustertUttakVedFødsel()).isEqualTo(foreldrepengerDto.uttaksplan().ønskerJustertUttakVedFødsel());
        assertThat(foreldrepenger.fordeling().perioder()).hasSameSizeAs(foreldrepengerDto.uttaksplan().uttaksperioder())
            .hasExactlyElementsOfTypes(UttaksPeriode.class, UttaksPeriode.class, UttaksPeriode.class)
            .extracting(LukketPeriodeMedVedlegg::getFom)
            .containsExactly(uttak.get(0).fom(), uttak.get(1).fom(), uttak.get(2).fom());
        assertThat(foreldrepenger.fordeling().perioder()).extracting(LukketPeriodeMedVedlegg::getTom)
            .containsExactly(uttak.get(0).tom(), uttak.get(1).tom(), uttak.get(2).tom());
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
        var annenNorskOpptjening = OpptjeningMaler.annenInntektNorsk(AnnenOpptjeningType.SLUTTPAKKE, LocalDate.now().minusYears(1), LocalDate.now());

        var vedlegg1 = DokumentasjonUtil.vedlegg(DokumentasjonUtil.barn());
        var vedlegg2 = DokumentasjonUtil.vedlegg(DokumentasjonUtil.uttaksperioder(List.of(new ÅpenPeriodeDtoOLD(NOW, NOW.plusWeeks(1).minusDays(1)),
            new ÅpenPeriodeDtoOLD(NOW.plusWeeks(4), NOW.plusWeeks(6).minusDays(1)))));
        var vedlegg3 = DokumentasjonUtil.vedlegg(DokumentasjonUtil.uttaksperiode(NOW.plusWeeks(1), NOW.plusWeeks(3).minusDays(1)));
        var vedlegg4 = DokumentasjonUtil.vedlegg(DokumentasjonUtil.opptjening(new ÅpenPeriodeDtoOLD(utenlandskOpptjening.fom(),
            utenlandskOpptjening.tom())));
        var vedlegg5 = DokumentasjonUtil.vedlegg(DokumentasjonUtil.opptjening(new ÅpenPeriodeDtoOLD(annenNorskOpptjening.fom(),
            annenNorskOpptjening.tom())));

        var søknadDto = new ForeldrepengerBuilder().medRolle(BrukerRolle.MOR)
            .medUttaksplan(new UttaksplanDto(true, uttak))
            .medDekningsgrad(Dekningsgrad.ÅTTI)
            .medAndreInntekterSiste10Mnd(List.of(utenlandskOpptjening, annenNorskOpptjening))
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
