package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FELLESPERIODE;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FORELDREPENGER_FØR_FØDSEL;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.MØDREKVOTE;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.UttakplanPeriodeBuilder.gradert;
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
import no.nav.foreldrepenger.common.domain.felles.opptjening.AnnenOpptjening;
import no.nav.foreldrepenger.common.domain.felles.opptjening.AnnenOpptjeningType;
import no.nav.foreldrepenger.common.domain.felles.opptjening.UtenlandskArbeidsforhold;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Foreldrepenger;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.GradertUttaksPeriode;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.LukketPeriodeMedVedlegg;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UttaksPeriode;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.Dekningsgrad;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.ForeldrepengesøknadDto;
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
        var uttak = List.of(
            uttak(FORELDREPENGER_FØR_FØDSEL, NOW.minusWeeks(3), NOW.minusDays(1)).build(),
            uttak(MØDREKVOTE, NOW, NOW.plusWeeks(15).minusDays(1)).build(),
            gradert(FELLESPERIODE, NOW.plusWeeks(15), NOW.plusWeeks(45).minusDays(1), 33.3)
                .build()
        );
        var søknadDto = new ForeldrepengerBuilder()
            .medFordeling(uttak)
            .medDekningsgrad(Dekningsgrad.HUNDRE)
            .medMedlemsskap(medlemsskapNorge())
            .medSøker(new SøkerBuilder(BrukerRolle.MOR)
                .medErAleneOmOmsorg(true)
                .medFrilansInformasjon(OpptjeningMaler.frilansOpptjening())
                .build())
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
        assertThat(foreldrepenger.medlemsskap().isBoddINorge()).isTrue();
        assertThat(foreldrepenger.medlemsskap().isNorgeNeste12()).isTrue();

        // Frilans
        var frilans = foreldrepenger.opptjening().frilans();
        var frilansDto = foreldrepengerDto.søker().frilansInformasjon();
        assertThat(frilans.periode().fom()).isEqualTo(frilansDto.oppstart());
        assertThat(frilans.harInntektFraFosterhjem()).isEqualTo(frilansDto.driverFosterhjem());
        assertThat(frilans.jobberFremdelesSomFrilans()).isEqualTo(frilansDto.jobberFremdelesSomFrilans());
        assertThat(frilans.nyOppstartet()).isFalse(); // Oppstart 2 år siden
        assertThat(frilans.frilansOppdrag()).hasSameSizeAs(frilansDto.oppdragForNæreVennerEllerFamilieSiste10Mnd());

        // Rettigheter
        var rettigheter = foreldrepenger.rettigheter();
        assertThat(rettigheter.harAleneOmsorgForBarnet()).isEqualTo(foreldrepengerDto.søker().erAleneOmOmsorg());
        assertThat(rettigheter.harAnnenForelderRett()).isEqualTo(foreldrepengerDto.annenForelder().harRettPåForeldrepenger());
        assertThat(rettigheter.harMorUføretrygd()).isEqualTo(foreldrepengerDto.annenForelder().harMorUføretrygd());
        assertThat(rettigheter.harAnnenForelderOppholdtSegIEØS()).isEqualTo(foreldrepengerDto.annenForelder().harAnnenForelderOppholdtSegIEØS());
        assertThat(rettigheter.harAnnenForelderTilsvarendeRettEØS()).isEqualTo(foreldrepengerDto.annenForelder().harAnnenForelderTilsvarendeRettEØS());

        // Fordeling
        assertThat(foreldrepenger.fordeling().ønskerJustertUttakVedFødsel()).isEqualTo(foreldrepengerDto.ønskerJustertUttakVedFødsel());
        assertThat(foreldrepenger.fordeling().perioder())
            .hasSameSizeAs(foreldrepengerDto.uttaksplan())
            .hasExactlyElementsOfTypes(
                UttaksPeriode.class,
                UttaksPeriode.class,
                GradertUttaksPeriode.class
            )
            .extracting(LukketPeriodeMedVedlegg::getFom)
            .containsExactly(
                uttak.get(0).tidsperiode().fom(),
                uttak.get(1).tidsperiode().fom(),
                uttak.get(2).tidsperiode().fom()
            );
        assertThat(foreldrepenger.fordeling().perioder())
            .extracting(LukketPeriodeMedVedlegg::getTom)
            .containsExactly(
                uttak.get(0).tidsperiode().tom(),
                uttak.get(1).tidsperiode().tom(),
                uttak.get(2).tidsperiode().tom()
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
            .medFordeling(uttak)
            .medØnskerJustertUttakVedFødsel(true)
            .medDekningsgrad(Dekningsgrad.ÅTTI)
            .medMedlemsskap(medlemskapUtlandetForrige12mnd())
            .medSøker(new SøkerBuilder(BrukerRolle.MOR)
                .medAndreInntekterSiste10Mnd(annenOpptjeninger)
                .build())
            .medAnnenForelder(AnnenforelderBuilder.annenpartIkkeRettOgMorHarUføretrygd(DUMMY_FNR).build())
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

        // Medlemsskap
        assertThat(foreldrepenger.medlemsskap().isBoddINorge()).isFalse();
        assertThat(foreldrepenger.medlemsskap().isNorgeNeste12()).isTrue();

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
        assertThat(rettigheter.harAnnenForelderTilsvarendeRettEØS()).isEqualTo(foreldrepengerDto.annenForelder().harAnnenForelderTilsvarendeRettEØS());

        // Fordeling
        assertThat(foreldrepenger.fordeling().ønskerJustertUttakVedFødsel()).isEqualTo(foreldrepengerDto.ønskerJustertUttakVedFødsel());
        assertThat(foreldrepenger.fordeling().perioder())
            .hasSameSizeAs(foreldrepengerDto.uttaksplan())
            .hasExactlyElementsOfTypes(
                UttaksPeriode.class,
                UttaksPeriode.class,
                UttaksPeriode.class
            )
            .extracting(LukketPeriodeMedVedlegg::getFom)
            .containsExactly(
                uttak.get(0).tidsperiode().fom(),
                uttak.get(1).tidsperiode().fom(),
                uttak.get(2).tidsperiode().fom()
            );
        assertThat(foreldrepenger.fordeling().perioder())
            .extracting(LukketPeriodeMedVedlegg::getTom)
            .containsExactly(
                uttak.get(0).tidsperiode().tom(),
                uttak.get(1).tidsperiode().tom(),
                uttak.get(2).tidsperiode().tom()
            );


    }
}
