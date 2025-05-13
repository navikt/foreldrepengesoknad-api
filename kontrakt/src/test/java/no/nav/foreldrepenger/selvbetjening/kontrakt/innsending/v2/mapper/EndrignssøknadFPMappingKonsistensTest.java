package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper;

import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FELLESPERIODE;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FORELDREPENGER_FØR_FØDSEL;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.DokumentasjonUtil.vedlegg;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.UttakplanPeriodeBuilder.utsettelse;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.UttakplanPeriodeBuilder.uttak;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.domain.felles.DokumentType;
import no.nav.foreldrepenger.common.domain.felles.VedleggReferanse;
import no.nav.foreldrepenger.common.domain.felles.annenforelder.NorskForelder;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Endringssøknad;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Foreldrepenger;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.LukketPeriodeMedVedlegg;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UtsettelsesÅrsak;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UttaksPeriode;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.DokumentasjonUtil;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.SøknadMapper;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.endringssøknad.EndringssøknadForeldrepengerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.AnnenforelderBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.BarnBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.EndringssøknadBuilder;

class EndrignssøknadFPMappingKonsistensTest {
    private static final LocalDate NOW = LocalDate.now();
    private static final Fødselsnummer DUMMY_FNR = new Fødselsnummer("0000000000");


    @Test
    void endringssøknadHappyCaseMappingKonsistesTest() {
        var uttak = List.of(uttak(FELLESPERIODE, NOW.minusWeeks(8), NOW.plusWeeks(5)).build());
        var søknadDto = new EndringssøknadBuilder(new Saksnummer("1")).medRolle(BrukerRolle.MOR)
            .medUttaksplan(uttak)
            .medAnnenForelder(AnnenforelderBuilder.aleneomsorgAnnenpartIkkeRettOgMorHarUføretrygd(DUMMY_FNR).build())
            .medBarn(BarnBuilder.omsorgsovertakelse(LocalDate.now().minusWeeks(2)).build())
            .build();
        var foreldrepengerDto = ((EndringssøknadForeldrepengerDto) søknadDto);

        var mappedSøknad = (Endringssøknad) SøknadMapper.tilSøknad(søknadDto, NOW);
        assertThat(mappedSøknad.getSaksnr()).isEqualTo(søknadDto.saksnummer());
        assertThat(mappedSøknad.getSøker().søknadsRolle()).isEqualTo(søknadDto.rolle());
        assertThat(mappedSøknad.getSøker().målform()).isEqualTo(søknadDto.språkkode());
        assertThat(mappedSøknad.getMottattdato()).isEqualTo(søknadDto.mottattdato());
        assertThat(mappedSøknad.getTilleggsopplysninger()).isEqualTo(foreldrepengerDto.tilleggsopplysninger());

        var ytelse = mappedSøknad.getYtelse();
        assertThat(ytelse).isInstanceOf(Foreldrepenger.class);
        var foreldrepenger = (Foreldrepenger) ytelse;

        // Annenpart
        assertThat(foreldrepenger.annenForelder()).isInstanceOf(NorskForelder.class);
        var norskforelder = (NorskForelder) foreldrepenger.annenForelder();
        var annenForelderDto = foreldrepengerDto.annenForelder();
        assertThat(norskforelder.fnr()).isEqualTo(annenForelderDto.fnr());

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
            .hasExactlyElementsOfTypes(UttaksPeriode.class)
            .extracting(LukketPeriodeMedVedlegg::getFom)
            .containsExactly(uttak.get(0).fom());
        assertThat(foreldrepenger.fordeling().perioder()).extracting(LukketPeriodeMedVedlegg::getTom).containsExactly(uttak.get(0).tom());
    }

    @Test
    void endringssøknadMedVedleggUttakOgBarnKorrektMapping() {
        var uttak = List.of(uttak(FORELDREPENGER_FØR_FØDSEL, NOW.minusWeeks(3), NOW.minusDays(1)).build(),
            utsettelse(UtsettelsesÅrsak.SYKDOM, NOW, NOW.plusWeeks(6)).build());
        var vedlegg1 = vedlegg(DokumentasjonUtil.barn());
        var vedlegg2 = vedlegg(DokumentType.I000063, DokumentasjonUtil.barn());
        var vedlegg3 = vedlegg(DokumentasjonUtil.uttaksperiode(uttak.getLast().fom(), uttak.getLast().tom()));
        var søknadDto = new EndringssøknadBuilder(new Saksnummer("1")).medRolle(BrukerRolle.MOR)
            .medUttaksplan(uttak)
            .medAnnenForelder(AnnenforelderBuilder.aleneomsorgAnnenpartIkkeRettOgMorHarUføretrygd(DUMMY_FNR).build())
            .medBarn(BarnBuilder.omsorgsovertakelse(LocalDate.now().minusWeeks(2)).build())
            .medVedlegg(List.of(vedlegg1, vedlegg2, vedlegg3))
            .build();

        var mappedSøknad = (Endringssøknad) SøknadMapper.tilSøknad(søknadDto, NOW);
        var ytelse = mappedSøknad.getYtelse();
        assertThat(ytelse).isInstanceOf(Foreldrepenger.class);
        var foreldrepenger = (Foreldrepenger) ytelse;

        assertThat(vedlegg1.referanse().verdi()).isNotNull();
        assertThat(vedlegg2.referanse().verdi()).isNotNull();
        assertThat(vedlegg3.referanse().verdi()).isNotNull();
        assertThat(foreldrepenger.relasjonTilBarn().getVedlegg()).extracting(VedleggReferanse::referanse)
            .containsExactly(vedlegg1.referanse().verdi(), vedlegg2.referanse().verdi());
        assertThat(foreldrepenger.fordeling().perioder().getLast().getVedlegg()).extracting(VedleggReferanse::referanse)
            .containsExactly(vedlegg3.referanse().verdi());
    }

}
