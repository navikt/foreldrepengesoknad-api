package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto;

import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FELLESPERIODE;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FORELDREPENGER_FØR_FØDSEL;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.MØDREKVOTE;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.TilretteleggingBuilder.delvis;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.TilretteleggingBuilder.hel;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.TilretteleggingBuilder.ingen;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.UttakplanPeriodeBuilder.gradert;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.UttakplanPeriodeBuilder.overføring;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.UttakplanPeriodeBuilder.uttak;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.maler.ArbeidsforholdMaler;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Orgnummer;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.Overføringsårsak;
import no.nav.foreldrepenger.common.mapper.DefaultJsonMapper;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.endringssøknad.EndringssøknadForeldrepengerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.engangsstønad.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.Dekningsgrad;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.SvangerskapspengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.AnnenforelderBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.BarnBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.EndringssøknadBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.EngangsstønadBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.ForeldrepengerBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.SvangerskapspengerBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.SøkerBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.maler.OpptjeningMaler;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.maler.UtenlandsoppholdMaler;

/**
 * Skal verifisere at seralisering => deseralisering av objektet ikke mister noe data på veien.
 * Fordrer at disse json-filene er validert
 */
class SøknadDtoJacksonRountripTest {

    private static final ObjectMapper MAPPER = DefaultJsonMapper.MAPPER;
    private static final LocalDate NOW = LocalDate.now();
    private static final Fødselsnummer DUMMY_FNR = new Fødselsnummer("0000000000");

    @Test
    void fpFarFellesperiodeHappycaseRountrip() throws IOException {
        var uttak = List.of(
            uttak(FELLESPERIODE, NOW.minusWeeks(8), NOW.plusWeeks(5)).build()
        );
        var søknad = new ForeldrepengerBuilder()
            .medUttaksplan(uttak)
            .medDekningsgrad(Dekningsgrad.HUNDRE)
            .medUtenlandsopphold(UtenlandsoppholdMaler.oppholdBareINorge())
            .medSøker(new SøkerBuilder(BrukerRolle.FAR).build())
            .medAnnenForelder(AnnenforelderBuilder.norskMedRettighetNorge(DUMMY_FNR).build())
            .medBarn(BarnBuilder.fødsel(1, LocalDate.now().minusWeeks(2)).build())
            .build();

        assertThat(søknad).isInstanceOf(ForeldrepengesøknadDto.class);
        test(søknad);
    }

    @Test
    void fpAdopsjonAnnenInntektRountripTest() throws IOException {
        var uttak = List.of(
            uttak(FORELDREPENGER_FØR_FØDSEL, NOW.minusWeeks(3), NOW.minusDays(1)).build(),
            uttak(MØDREKVOTE, NOW, NOW.plusWeeks(15).minusDays(1)).build(),
            uttak(FELLESPERIODE, NOW.plusWeeks(15), NOW.plusWeeks(31).minusDays(1)).build()
        );
        var søknad = new ForeldrepengerBuilder()
            .medUttaksplan(uttak)
            .medDekningsgrad(Dekningsgrad.ÅTTI)
            .medUtenlandsopphold(UtenlandsoppholdMaler.oppholdBareINorge())
            .medSøker(new SøkerBuilder(BrukerRolle.MOR)
                .medAndreInntekterSiste10Mnd(List.of(OpptjeningMaler.utenlandskArbeidsforhold(CountryCode.US)))
                .build())
            .medAnnenForelder(AnnenforelderBuilder.annenpartIkkeRettOgMorHarUføretrygd(DUMMY_FNR).build())
            .medBarn(BarnBuilder.adopsjon(LocalDate.now().minusWeeks(2), false).build())
            .build();

        assertThat(søknad).isInstanceOf(ForeldrepengesøknadDto.class);
        test(søknad);
    }

    @Test
    void fpGraderingFrilansTermingRountripTest() throws IOException {
        var uttak = List.of(
            uttak(FORELDREPENGER_FØR_FØDSEL, NOW.minusWeeks(3), NOW.minusDays(1)).build(),
            uttak(MØDREKVOTE, NOW, NOW.plusWeeks(15).minusDays(1)).build(),
            gradert(FELLESPERIODE, NOW.plusWeeks(15), NOW.plusWeeks(45).minusDays(1), 55.0).build()
        );
        var søknad = new ForeldrepengerBuilder()
            .medUttaksplan(uttak)
            .medDekningsgrad(Dekningsgrad.HUNDRE)
            .medUtenlandsopphold(UtenlandsoppholdMaler.oppholdBareINorge())
            .medSøker(new SøkerBuilder(BrukerRolle.MOR)
                .medFrilansInformasjon(OpptjeningMaler.frilansOpptjening())
                .build())
            .medAnnenForelder(AnnenforelderBuilder.norskMedRettighetNorge(DUMMY_FNR).build())
            .medBarn(BarnBuilder.termin(2, LocalDate.now().minusWeeks(2)).build())
            .build();

        assertThat(søknad).isInstanceOf(ForeldrepengesøknadDto.class);
        test(søknad);
    }

    @Test
    void fpMorGraderingEgenNæringOgFrilansRountripTest() throws IOException {
        var orgnummerNæring = "00000";
        var uttak = List.of(
            uttak(FORELDREPENGER_FØR_FØDSEL, NOW.minusWeeks(3), NOW.minusDays(1)).build(),
            uttak(MØDREKVOTE, NOW, NOW.plusWeeks(15).minusDays(1)).build(),
            gradert(FELLESPERIODE, NOW.plusWeeks(15), NOW.plusWeeks(45).minusDays(1), 20.0)
                .medOrgnumre(List.of(orgnummerNæring))
                .build()
        );
        var søknad = new ForeldrepengerBuilder()
            .medUttaksplan(uttak)
            .medDekningsgrad(Dekningsgrad.HUNDRE)
            .medUtenlandsopphold(UtenlandsoppholdMaler.oppholdBareINorge())
            .medSøker(new SøkerBuilder(BrukerRolle.MOR)
                .medFrilansInformasjon(OpptjeningMaler.frilansOpptjening())
                .medSelvstendigNæringsdrivendeInformasjon(List.of(OpptjeningMaler.egenNaeringOpptjening(orgnummerNæring)))
                .build())
            .medAnnenForelder(AnnenforelderBuilder.norskMedRettighetNorge(DUMMY_FNR).build())
            .medBarn(BarnBuilder.fødsel(1, LocalDate.now().minusWeeks(2)).build())
            .build();

        assertThat(søknad).isInstanceOf(ForeldrepengesøknadDto.class);
        test(søknad);
    }

    @Test
    void farOmsorgsovertagelseMorUføreRoundtripTest() throws IOException {
        var uttak = List.of(
            overføring(Overføringsårsak.SYKDOM_ANNEN_FORELDER, MØDREKVOTE, NOW, NOW.plusWeeks(15).minusDays(1)).build(),
            uttak(FELLESPERIODE, NOW.plusWeeks(15), NOW.plusWeeks(45).minusDays(1)).build()
        );
        var søknad = new ForeldrepengerBuilder()
            .medUttaksplan(uttak)
            .medDekningsgrad(Dekningsgrad.HUNDRE)
            .medUtenlandsopphold(UtenlandsoppholdMaler.oppholdIUtlandetForrige12mnd())
            .medSøker(new SøkerBuilder(BrukerRolle.MOR)
                .medErAleneOmOmsorg(true)
                .build())
            .medAnnenForelder(AnnenforelderBuilder.annenpartIkkeRettOgMorHarUføretrygd(DUMMY_FNR).build())
            .medBarn(BarnBuilder.omsorgsovertakelse(LocalDate.now().minusWeeks(2)).build())
            .build();

        assertThat(søknad).isInstanceOf(ForeldrepengesøknadDto.class);
        test(søknad);
    }

    @Test
    void svangerskapspengerRountripTest() throws IOException {
        var tilrettelegginger = List.of(
            hel(NOW.minusMonths(1), NOW.minusMonths(1), ArbeidsforholdMaler.selvstendigNæringsdrivende()).build(),
            delvis(NOW, NOW, ArbeidsforholdMaler.privatArbeidsgiver(DUMMY_FNR), 55.0).build(),
            ingen(NOW.plusWeeks(1), NOW.plusWeeks(1), ArbeidsforholdMaler.virksomhet(Orgnummer.MAGIC_ORG)).build()
        );
        var søknad = new SvangerskapspengerBuilder(tilrettelegginger)
            .medUtenlandsopphold(UtenlandsoppholdMaler.oppholdIUtlandetForrige12mnd())
            .medSelvstendigNæringsdrivendeInformasjon(List.of(OpptjeningMaler.egenNaeringOpptjening(Orgnummer.MAGIC_ORG.value())))
            .medBarn(BarnBuilder.termin(2, LocalDate.now().plusWeeks(2)).build())
            .build();

        assertThat(søknad).isInstanceOf(SvangerskapspengesøknadDto.class);
        test(søknad);
    }

    @Test
    void engangsstønadRountripTest() throws IOException {
        var søknad = new EngangsstønadBuilder()
            .medSpråkkode(Målform.NB)
            .medUtenlandsopphold(UtenlandsoppholdMaler.oppholdIUtlandetForrige12mnd())
            .medBarn(BarnBuilder.fødsel(1, NOW).build())
            .build();

        assertThat(søknad).isInstanceOf(EngangsstønadDto.class);
        test(søknad);
    }
    @Test
    void endringssøknadFpRountripTest() throws IOException {
        var uttak = List.of(
            uttak(FELLESPERIODE, NOW.minusWeeks(8), NOW.plusWeeks(5)).build()
        );
        var søknad = new EndringssøknadBuilder(new Saksnummer("1"))
            .medUttaksplan(uttak)
            .medSøker(new SøkerBuilder(BrukerRolle.MOR)
                .medErAleneOmOmsorg(true)
                .build())
            .medAnnenForelder(AnnenforelderBuilder.annenpartIkkeRettOgMorHarUføretrygd(DUMMY_FNR).build())
            .medBarn(BarnBuilder.omsorgsovertakelse(LocalDate.now().minusWeeks(2)).build())
            .build();

        assertThat(søknad).isInstanceOf(EndringssøknadForeldrepengerDto.class);
        test(søknad);
    }



    private void test(Object object) throws IOException {
        assertEquals(object, MAPPER.readValue(write(object), object.getClass()));
    }

    private String write(Object obj) throws JsonProcessingException {
        return MAPPER.writeValueAsString(obj);
    }
}
