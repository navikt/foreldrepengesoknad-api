package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper;

import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FELLESPERIODE;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FORELDREPENGER_FØR_FØDSEL;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.MØDREKVOTE;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.UttakplanPeriodeBuilder.gradert;
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
import no.nav.foreldrepenger.common.domain.felles.annenforelder.NorskForelder;
import no.nav.foreldrepenger.common.domain.felles.annenforelder.UkjentForelder;
import no.nav.foreldrepenger.common.domain.felles.opptjening.AnnenOpptjening;
import no.nav.foreldrepenger.common.domain.felles.opptjening.AnnenOpptjeningType;
import no.nav.foreldrepenger.common.domain.felles.opptjening.UtenlandskArbeidsforhold;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Foreldrepenger;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.GradertUttaksPeriode;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.LukketPeriodeMedVedlegg;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UttaksPeriode;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.SøknadMapper;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.Dekningsgrad;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.UttaksplanDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.AnnenforelderBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.BarnBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.ForeldrepengerBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.SøkerBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.maler.OpptjeningMaler;

class ForeldrepengesoknadMappingKonsistensTest {
    private static final LocalDate NOW = LocalDate.now();
    private static final Fødselsnummer DUMMY_FNR = new Fødselsnummer("0000000000");


    @Test
    void fpGraderingFrilansAleneomsorgFarIkkeRettMappingKonsistensTest() {
        var uttak = List.of(
            uttak(FORELDREPENGER_FØR_FØDSEL, NOW.minusWeeks(3), NOW.minusDays(1)).build(),
            uttak(MØDREKVOTE, NOW, NOW.plusWeeks(15).minusDays(1)).build(),
            gradert(FELLESPERIODE, NOW.plusWeeks(15), NOW.plusWeeks(45).minusDays(1), 33.3).build()
        );
        var søknadDto = new ForeldrepengerBuilder()
            .medUttaksplan(uttak)
            .medDekningsgrad(Dekningsgrad.HUNDRE)
            .medUtenlandsopphold(oppholdBareINorge())
            .medSøker(new SøkerBuilder(BrukerRolle.MOR)
                .medErAleneOmOmsorg(true)
                .medFrilansInformasjon(OpptjeningMaler.frilansOpptjening())
                .build())
            .medAnnenForelder(AnnenforelderBuilder.norskIkkeRett(DUMMY_FNR).build())
            .medBarn(BarnBuilder.termin(2, LocalDate.now().minusWeeks(2)).build())
            .build();
        var foreldrepengerDto = ((ForeldrepengesøknadDto) søknadDto);

        var mappedSøknad = SøknadMapper.tilSøknad(søknadDto, NOW);
        assertThat(mappedSøknad.getSøker().søknadsRolle()).isEqualTo(foreldrepengerDto.søker().rolle());
        assertThat(mappedSøknad.getSøker().målform()).isEqualTo(foreldrepengerDto.søker().språkkode());
        assertThat(mappedSøknad.getMottattdato()).isEqualTo(søknadDto.mottattdato());
        assertThat(mappedSøknad.getTilleggsopplysninger()).isEqualTo(foreldrepengerDto.tilleggsopplysninger());

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
        var frilansDto = foreldrepengerDto.søker().frilansInformasjon();
        assertThat(frilans.periode().fom()).isEqualTo(frilansDto.oppstart());
        assertThat(frilans.jobberFremdelesSomFrilans()).isEqualTo(frilansDto.jobberFremdelesSomFrilans());

        // Rettigheter
        var rettigheter = foreldrepenger.rettigheter();
        assertThat(rettigheter.harAleneOmsorgForBarnet()).isEqualTo(foreldrepengerDto.søker().erAleneOmOmsorg());
        assertThat(rettigheter.harAnnenForelderRett()).isEqualTo(annenForelderDto.rettigheter().harRettPåForeldrepenger());
        assertThat(rettigheter.harMorUføretrygd()).isEqualTo(annenForelderDto.rettigheter().harMorUføretrygd());
        assertThat(rettigheter.harAnnenForelderOppholdtSegIEØS()).isEqualTo(annenForelderDto.rettigheter().harAnnenForelderOppholdtSegIEØS());
        assertThat(rettigheter.harAnnenForelderTilsvarendeRettEØS()).isEqualTo(annenForelderDto.rettigheter().harAnnenForelderTilsvarendeRettEØS());

        // Fordeling
        assertThat(foreldrepenger.fordeling().ønskerJustertUttakVedFødsel()).isEqualTo(foreldrepengerDto.uttaksplan().ønskerJustertUttakVedFødsel());
        assertThat(foreldrepenger.fordeling().perioder())
            .hasSameSizeAs(foreldrepengerDto.uttaksplan().uttaksperioder())
            .hasExactlyElementsOfTypes(
                UttaksPeriode.class,
                UttaksPeriode.class,
                GradertUttaksPeriode.class
            )
            .extracting(LukketPeriodeMedVedlegg::getFom)
            .containsExactly(
                uttak.get(0).fom(),
                uttak.get(1).fom(),
                uttak.get(2).fom()
            );
        assertThat(foreldrepenger.fordeling().perioder())
            .extracting(LukketPeriodeMedVedlegg::getTom)
            .containsExactly(
                uttak.get(0).tom(),
                uttak.get(1).tom(),
                uttak.get(2).tom()
            );

    }

    @Test
    void fpAdopsjonAnnenInntektMappingKonsistensTest() {
        var uttak = List.of(
            uttak(FORELDREPENGER_FØR_FØDSEL, NOW.minusWeeks(3), NOW.minusDays(1)).build(),
            uttak(MØDREKVOTE, NOW, NOW.plusWeeks(15).minusDays(1)).build(),
            uttak(FELLESPERIODE, NOW.plusWeeks(15), NOW.plusWeeks(31).minusDays(1)).build()
        );
        var utenlandskOpptjening = OpptjeningMaler.utenlandskArbeidsforhold(CountryCode.US);
        var annenNorskOpptjening = OpptjeningMaler.annenInntektNorsk(AnnenOpptjeningType.SLUTTPAKKE);
        var annenOpptjeninger = List.of(utenlandskOpptjening, annenNorskOpptjening);
        var søknadDto = new ForeldrepengerBuilder()
            .medUttaksplan(new UttaksplanDto(true, uttak))
            .medDekningsgrad(Dekningsgrad.ÅTTI)
            .medUtenlandsopphold(oppholdIUtlandetForrige12mnd())
            .medSøker(new SøkerBuilder(BrukerRolle.MOR)
                .medAndreInntekterSiste10Mnd(annenOpptjeninger)
                .build())
            .medAnnenForelder(AnnenforelderBuilder.ukjentForelder())
            .medBarn(BarnBuilder.adopsjon(LocalDate.now().minusWeeks(2), false).build())
            .build();
        var foreldrepengerDto = ((ForeldrepengesøknadDto) søknadDto);

        var mappedSøknad = SøknadMapper.tilSøknad(søknadDto, NOW);
        assertThat(mappedSøknad.getSøker().søknadsRolle()).isEqualTo(foreldrepengerDto.søker().rolle());
        assertThat(mappedSøknad.getSøker().målform()).isEqualTo(foreldrepengerDto.søker().språkkode());
        assertThat(mappedSøknad.getMottattdato()).isEqualTo(søknadDto.mottattdato());
        assertThat(mappedSøknad.getTilleggsopplysninger()).isEqualTo(foreldrepengerDto.tilleggsopplysninger());

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
            .containsExactly(AnnenOpptjeningType.valueOf(annenNorskOpptjening.type()));
        assertThat(foreldrepenger.opptjening().utenlandskArbeidsforhold()).hasSize(1)
            .extracting(UtenlandskArbeidsforhold::land)
            .containsExactly(CountryCode.valueOf(utenlandskOpptjening.land()));

        // Rettigheter
        var rettigheter = foreldrepenger.rettigheter();
        assertThat(rettigheter.harAleneOmsorgForBarnet()).isEqualTo(foreldrepengerDto.søker().erAleneOmOmsorg());
        assertThat(rettigheter.harAnnenForelderRett()).isFalse();
        assertThat(rettigheter.harMorUføretrygd()).isNull();
        assertThat(rettigheter.harAnnenForelderOppholdtSegIEØS()).isNull();
        assertThat(rettigheter.harAnnenForelderTilsvarendeRettEØS()).isNull();

        // Fordeling
        assertThat(foreldrepenger.fordeling().ønskerJustertUttakVedFødsel()).isEqualTo(foreldrepengerDto.uttaksplan().ønskerJustertUttakVedFødsel());
        assertThat(foreldrepenger.fordeling().perioder())
            .hasSameSizeAs(foreldrepengerDto.uttaksplan().uttaksperioder())
            .hasExactlyElementsOfTypes(
                UttaksPeriode.class,
                UttaksPeriode.class,
                UttaksPeriode.class
            )
            .extracting(LukketPeriodeMedVedlegg::getFom)
            .containsExactly(
                uttak.get(0).fom(),
                uttak.get(1).fom(),
                uttak.get(2).fom()
            );
        assertThat(foreldrepenger.fordeling().perioder())
            .extracting(LukketPeriodeMedVedlegg::getTom)
            .containsExactly(
                uttak.get(0).tom(),
                uttak.get(1).tom(),
                uttak.get(2).tom()
            );


    }
}
