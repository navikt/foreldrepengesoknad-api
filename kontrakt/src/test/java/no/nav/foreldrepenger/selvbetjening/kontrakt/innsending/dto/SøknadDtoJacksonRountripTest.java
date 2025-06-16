package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FELLESPERIODE;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FORELDREPENGER_FØR_FØDSEL;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.MØDREKVOTE;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.UttakplanPeriodeBuilder.gradert;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.UttakplanPeriodeBuilder.overføring;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.UttakplanPeriodeBuilder.uttak;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadForeldrepengerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.Dekningsgrad;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.SvangerskapspengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.AnnenforelderBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.BarnBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.EndringssøknadBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.EngangsstønadBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.ForeldrepengerBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.SvangerskapspengerBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.TilretteleggingBehovBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler.ArbeidsforholdMaler;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler.OpptjeningMaler;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler.UtenlandsoppholdMaler;

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
        var uttak = List.of(uttak(FELLESPERIODE, NOW.minusWeeks(8), NOW.plusWeeks(5)).build());
        var søknad = new ForeldrepengerBuilder().medRolle(BrukerRolle.FAR)
            .medUttaksplan(uttak)
            .medDekningsgrad(Dekningsgrad.HUNDRE)
            .medUtenlandsopphold(UtenlandsoppholdMaler.oppholdBareINorge())
            .medAnnenForelder(AnnenforelderBuilder.norskMedRettighetNorge(DUMMY_FNR).build())
            .medBarn(BarnBuilder.fødsel(1, LocalDate.now().minusWeeks(2)).build())
            .build();

        assertThat(søknad).isInstanceOf(ForeldrepengesøknadDto.class);
        test(søknad);
    }

    @Test
    void fpAdopsjonAnnenInntektRountripTest() throws IOException {
        var uttak = List.of(uttak(FORELDREPENGER_FØR_FØDSEL, NOW.minusWeeks(3), NOW.minusDays(1)).build(),
            uttak(MØDREKVOTE, NOW, NOW.plusWeeks(15).minusDays(1)).build(),
            uttak(FELLESPERIODE, NOW.plusWeeks(15), NOW.plusWeeks(31).minusDays(1)).build());
        var søknad = new ForeldrepengerBuilder().medUttaksplan(uttak)
            .medRolle(BrukerRolle.MOR)
            .medDekningsgrad(Dekningsgrad.ÅTTI)
            .medUtenlandsopphold(UtenlandsoppholdMaler.oppholdBareINorge())
            .medAndreInntekterSiste10Mnd(List.of(OpptjeningMaler.utenlandskArbeidsforhold(CountryCode.US)))
            .medAnnenForelder(AnnenforelderBuilder.aleneomsorgAnnenpartIkkeRettOgMorHarUføretrygd(DUMMY_FNR).build())
            .medBarn(BarnBuilder.adopsjon(LocalDate.now().minusWeeks(2), false).build())
            .build();

        assertThat(søknad).isInstanceOf(ForeldrepengesøknadDto.class);
        test(søknad);
    }

    @Test
    void fpGraderingFrilansTermingRountripTest() throws IOException {
        var uttak = List.of(uttak(FORELDREPENGER_FØR_FØDSEL, NOW.minusWeeks(3), NOW.minusDays(1)).build(),
            uttak(MØDREKVOTE, NOW, NOW.plusWeeks(15).minusDays(1)).build(),
            gradert(FELLESPERIODE, NOW.plusWeeks(15), NOW.plusWeeks(45).minusDays(1), 55.0).build());
        var søknad = new ForeldrepengerBuilder().medUttaksplan(uttak)
            .medRolle(BrukerRolle.MOR)
            .medDekningsgrad(Dekningsgrad.HUNDRE)
            .medUtenlandsopphold(UtenlandsoppholdMaler.oppholdBareINorge())
            .medFrilansInformasjon(OpptjeningMaler.frilansOpptjening())
            .medAnnenForelder(AnnenforelderBuilder.norskMedRettighetNorge(DUMMY_FNR).build())
            .medBarn(BarnBuilder.termin(2, LocalDate.now().minusWeeks(2)).build())
            .build();

        assertThat(søknad).isInstanceOf(ForeldrepengesøknadDto.class);
        test(søknad);
    }

    @Test
    void fpMorGraderingEgenNæringOgFrilansRountripTest() throws IOException {
        var orgnummerNæring = "00000";
        var uttak = List.of(uttak(FORELDREPENGER_FØR_FØDSEL, NOW.minusWeeks(3), NOW.minusDays(1)).build(),
            uttak(MØDREKVOTE, NOW, NOW.plusWeeks(15).minusDays(1)).build(),
            gradert(FELLESPERIODE, NOW.plusWeeks(15), NOW.plusWeeks(45).minusDays(1), 20.0).medOrgnumre(List.of(orgnummerNæring)).build());
        var søknad = new ForeldrepengerBuilder().medUttaksplan(uttak)
            .medRolle(BrukerRolle.MOR)
            .medDekningsgrad(Dekningsgrad.HUNDRE)
            .medUtenlandsopphold(UtenlandsoppholdMaler.oppholdBareINorge())
            .medFrilansInformasjon(OpptjeningMaler.frilansOpptjening())
            .medSelvstendigNæringsdrivendeInformasjon(OpptjeningMaler.egenNaeringOpptjening(orgnummerNæring))
            .medAnnenForelder(AnnenforelderBuilder.norskMedRettighetNorge(DUMMY_FNR).build())
            .medBarn(BarnBuilder.fødsel(1, LocalDate.now().minusWeeks(2)).build())
            .build();

        assertThat(søknad).isInstanceOf(ForeldrepengesøknadDto.class);
        test(søknad);
    }

    @Test
    void farOmsorgsovertagelseMorUføreRoundtripTest() throws IOException {
        var uttak = List.of(overføring(Overføringsårsak.SYKDOM_ANNEN_FORELDER, MØDREKVOTE, NOW, NOW.plusWeeks(15).minusDays(1)).build(),
            uttak(FELLESPERIODE, NOW.plusWeeks(15), NOW.plusWeeks(45).minusDays(1)).build());
        var søknad = new ForeldrepengerBuilder().medRolle(BrukerRolle.MOR)
            .medUttaksplan(uttak)
            .medDekningsgrad(Dekningsgrad.HUNDRE)
            .medUtenlandsopphold(UtenlandsoppholdMaler.oppholdIUtlandetForrige12mnd())
            .medAnnenForelder(AnnenforelderBuilder.aleneomsorgAnnenpartIkkeRettOgMorHarUføretrygd(DUMMY_FNR).build())
            .medBarn(BarnBuilder.omsorgsovertakelse(LocalDate.now().minusWeeks(2)).build())
            .build();

        assertThat(søknad).isInstanceOf(ForeldrepengesøknadDto.class);
        test(søknad);
    }

    @Test
    void svangerskapspengerBehovRountripTest() throws IOException {
        var tilretteleggingbehov = List.of(new TilretteleggingBehovBuilder(ArbeidsforholdMaler.selvstendigNæringsdrivende(), NOW.minusMonths(1)).hel(
                NOW.minusMonths(1)).build(),
            new TilretteleggingBehovBuilder(ArbeidsforholdMaler.privatArbeidsgiver(DUMMY_FNR), NOW).delvis(NOW, 55.0).build(),
            new TilretteleggingBehovBuilder(ArbeidsforholdMaler.virksomhet(Orgnummer.MAGIC_ORG), NOW.plusMonths(1)).ingen(NOW.plusMonths(1)).build());

        var søknad = new SvangerskapspengerBuilder(tilretteleggingbehov).medUtenlandsopphold(UtenlandsoppholdMaler.oppholdIUtlandetForrige12mnd())
            .medSelvstendigNæringsdrivendeInformasjon(OpptjeningMaler.egenNaeringOpptjening(Orgnummer.MAGIC_ORG.value()))
            .medBarn(BarnBuilder.termin(2, LocalDate.now().plusWeeks(2)).build())
            .build();

        assertThat(søknad).isInstanceOf(SvangerskapspengesøknadDto.class);
        test(søknad);
    }

    @Test
    void engangsstønadRountripTest() throws IOException {
        var søknad = new EngangsstønadBuilder().medSpråkkode(Målform.NB)
            .medUtenlandsopphold(UtenlandsoppholdMaler.oppholdIUtlandetForrige12mnd())
            .medBarn(BarnBuilder.fødsel(1, NOW).build())
            .build();

        assertThat(søknad).isInstanceOf(EngangsstønadDto.class);
        test(søknad);
    }

    @Test
    void endringssøknadFpRountripTest() throws IOException {
        var uttak = List.of(uttak(FELLESPERIODE, NOW.minusWeeks(8), NOW.plusWeeks(5)).build());
        var søknad = new EndringssøknadBuilder(new Saksnummer("1")).medRolle(BrukerRolle.MOR)
            .medUttaksplan(uttak)
            .medAnnenForelder(AnnenforelderBuilder.aleneomsorgAnnenpartIkkeRettOgMorHarUføretrygd(DUMMY_FNR).build())
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
