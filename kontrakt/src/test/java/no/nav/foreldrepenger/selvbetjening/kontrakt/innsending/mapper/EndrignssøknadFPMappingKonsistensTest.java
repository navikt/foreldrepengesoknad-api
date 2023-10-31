package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FELLESPERIODE;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.UttakplanPeriodeBuilder.uttak;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.domain.felles.annenforelder.NorskForelder;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Endringssøknad;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Foreldrepenger;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.LukketPeriodeMedVedlegg;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UttaksPeriode;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadForeldrepengerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.AnnenforelderBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.BarnBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.EndringssøknadBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.SøkerBuilder;

class EndrignssøknadFPMappingKonsistensTest {
    private static final LocalDate NOW = LocalDate.now();
    private static final Fødselsnummer DUMMY_FNR = new Fødselsnummer("0000000000");


    @Test
    void endringssøknadHappyCaseMappingKonsistesTest() {
        var uttak = List.of(
            uttak(FELLESPERIODE, NOW.minusWeeks(8), NOW.plusWeeks(5)).build()
        );
        var søknadDto = new EndringssøknadBuilder(new Saksnummer("1"))
            .medFordeling(uttak)
            .medSøker(new SøkerBuilder(BrukerRolle.MOR)
                .medErAleneOmOmsorg(true)
                .build())
            .medAnnenForelder(AnnenforelderBuilder.annenpartIkkeRettOgMorHarUføretrygd(DUMMY_FNR).build())
            .medBarn(BarnBuilder.omsorgsovertakelse(LocalDate.now().minusWeeks(2)).build())
            .build();
        var foreldrepengerDto = ((EndringssøknadForeldrepengerDto) søknadDto);

        var mappedSøknad = (Endringssøknad) SøknadMapper.tilSøknad(søknadDto, NOW);
        assertThat(mappedSøknad.getSaksnr()).isEqualTo(søknadDto.saksnummer());
        assertThat(mappedSøknad.getSøker().søknadsRolle()).isEqualTo(søknadDto.søker().rolle());
        assertThat(mappedSøknad.getSøker().målform()).isEqualTo(søknadDto.søker().språkkode());
        assertThat(mappedSøknad.getMottattdato()).isEqualTo(søknadDto.mottattdato());
        assertThat(mappedSøknad.getTilleggsopplysninger()).isEqualTo(foreldrepengerDto.tilleggsopplysninger());

        var ytelse = mappedSøknad.getYtelse();
        assertThat(ytelse).isInstanceOf(Foreldrepenger.class);
        var foreldrepenger = (Foreldrepenger) ytelse;

        // Annenpart
        assertThat(foreldrepenger.annenForelder()).isInstanceOf(NorskForelder.class);
        var norskforelder = (NorskForelder) foreldrepenger.annenForelder();
        assertThat(norskforelder.fnr().value()).isEqualTo(foreldrepengerDto.annenForelder().fnr());

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
                UttaksPeriode.class
            )
            .extracting(LukketPeriodeMedVedlegg::getFom)
            .containsExactly(
                uttak.get(0).tidsperiode().fom()
            );
        assertThat(foreldrepenger.fordeling().perioder())
            .extracting(LukketPeriodeMedVedlegg::getTom)
            .containsExactly(
                uttak.get(0).tidsperiode().tom()
            );
    }

}
