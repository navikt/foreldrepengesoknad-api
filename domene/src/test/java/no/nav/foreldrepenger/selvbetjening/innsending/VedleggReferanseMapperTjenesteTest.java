package no.nav.foreldrepenger.selvbetjening.innsending;

import static no.nav.foreldrepenger.common.domain.felles.InnsendingsType.LASTET_OPP;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FELLESPERIODE;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.FORELDREPENGER_FØR_FØDSEL;
import static no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType.MØDREKVOTE;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.UttakplanPeriodeBuilder.gradert;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.UttakplanPeriodeBuilder.uttak;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler.MedlemsskapMaler.medlemsskapNorge;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Orgnummer;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.AnnenInntektDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.Dekningsgrad;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.TilretteleggingDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ÅpenPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.AnnenforelderBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.BarnBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.BarnV2Builder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.EngangsstønadV2Builder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.ForeldrepengerBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.SvangerskapspengerBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.SøkerBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.TilretteleggingBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler.ArbeidsforholdMaler;

class VedleggReferanseMapperTjenesteTest {

    private static final Fødselsnummer DUMMY_FNR = new Fødselsnummer("9999999999");

    @Test
    void foreldrepengerMedUttaksdokumentasjon() {
        var NOW = LocalDate.now();
        var uttak = List.of(
            uttak(FORELDREPENGER_FØR_FØDSEL, NOW.minusWeeks(3), NOW.minusDays(1)).build(),
            uttak(MØDREKVOTE, NOW, NOW.plusWeeks(15).minusDays(1)).build(),
            gradert(FELLESPERIODE, NOW.plusWeeks(15), NOW.plusWeeks(45).minusDays(1), 33.3).build()
        );

        var annenInntekt = new AnnenInntektDto(
            "JOBB_I_UTLANDET",
            CountryCode.UK.getAlpha2(),
            "elskap AS",
            new ÅpenPeriodeDto(LocalDate.now().minusYears(4), LocalDate.now()),
            false,
            new ArrayList<>()
        );
        var søker = new SøkerBuilder(BrukerRolle.FAR)
            .medAndreInntekterSiste10Mnd(List.of(annenInntekt))
            .build();
        var dokumenterer = new VedleggDto.Dokumenterer(
            VedleggDto.Dokumenterer.Type.UTTAK,
            null,
            List.of(
                new ÅpenPeriodeDto(uttak.get(0).tidsperiode().fom(), uttak.get(0).tidsperiode().tom()),
                new ÅpenPeriodeDto(uttak.get(1).tidsperiode().fom(), uttak.get(1).tidsperiode().tom())
            )
        );
        var vedlegg1 = vedlegg(dokumenterer);
        var vedlegg2 = vedlegg(dokumenterer);
        var søknad = (ForeldrepengesøknadDto) new ForeldrepengerBuilder()
            .medFordeling(uttak)
            .medDekningsgrad(Dekningsgrad.HUNDRE)
            .medMedlemsskap(medlemsskapNorge())
            .medSøker(søker)
            .medAnnenForelder(AnnenforelderBuilder.norskMedRettighetNorge(DUMMY_FNR).build())
            .medBarn(BarnBuilder.fødsel(1, LocalDate.now().minusWeeks(2)).build())
            .medVedlegg(List.of(vedlegg1, vedlegg2))
            .build();

        VedleggReferanseMapperTjeneste.leggVedleggsreferanserTilSøknad(søknad);
        assertThat(søknad.uttaksplan().get(0).vedlegg()).containsExactly(vedlegg1.getId(), vedlegg2.getId());
        assertThat(søknad.uttaksplan().get(1).vedlegg()).containsExactly(vedlegg1.getId(), vedlegg2.getId());
        assertThat(søknad.uttaksplan().get(2).vedlegg()).isEmpty();
    }

    @Test
    void engangsstønadDokumentasjonAvAdopsjon() {
        var dokumenterer = new VedleggDto.Dokumenterer(
            VedleggDto.Dokumenterer.Type.BARN,
            null,
            null
        );
        var vedlegg = vedlegg(dokumenterer);
        var engangstønad = new EngangsstønadV2Builder()
            .medSpråkkode(Målform.NB)
            .medBarn(BarnV2Builder.adopsjon(LocalDate.now().minusWeeks(2), false).build())
            .medVedlegg(List.of(vedlegg))
            .build();

        assertThat(engangstønad.barn().vedleggreferanser()).isEmpty();

        VedleggReferanseMapperTjeneste.leggVedleggsreferanserTilSøknad(engangstønad);

        assertThat(engangstønad.barn().vedleggreferanser()).containsExactly(vedlegg.getId());
    }

    @Test
    void svangersskapspengerTilretteleggingDokumentasjon() {
        var virksomhet = ArbeidsforholdMaler.virksomhet(Orgnummer.MAGIC_ORG);
        var delvis = TilretteleggingBuilder.delvis(LocalDate.now(), LocalDate.now().minusMonths(1), virksomhet, 70.0).build();
        var næring = ArbeidsforholdMaler.selvstendigNæringsdrivende();
        var ingen = TilretteleggingBuilder.ingen(LocalDate.now(), LocalDate.now().minusMonths(1), næring).build();
        var privat = ArbeidsforholdMaler.privatArbeidsgiver("000000000");
        var hel = TilretteleggingBuilder.hel(LocalDate.now(), LocalDate.now().minusMonths(1), privat).build();
        var tilrettelegginger = List.of(hel, delvis, ingen);

        var vedleggBarn = vedlegg(new VedleggDto.Dokumenterer(VedleggDto.Dokumenterer.Type.BARN, null, null));
        var vedleggVirksomhet = vedlegg(new VedleggDto.Dokumenterer(VedleggDto.Dokumenterer.Type.TILRETTELEGGING, virksomhet, null));
        var vedleggNæring = vedlegg(new VedleggDto.Dokumenterer(VedleggDto.Dokumenterer.Type.TILRETTELEGGING, næring, null));
        var vedleggPrivat = vedlegg(new VedleggDto.Dokumenterer(VedleggDto.Dokumenterer.Type.TILRETTELEGGING, privat, null));

        var engangstønad = new SvangerskapspengerBuilder(tilrettelegginger)
            .medBarn(BarnBuilder.termin(2, LocalDate.now().minusWeeks(2)).build())
            .medVedlegg(List.of(vedleggBarn, vedleggVirksomhet, vedleggNæring, vedleggPrivat))
            .build();

        assertThat(engangstønad.barn().getAlleVedlegg()).isEmpty();
        assertThat(engangstønad.tilrettelegging())
            .flatMap(TilretteleggingDto::vedlegg)
            .isEmpty();

        VedleggReferanseMapperTjeneste.leggVedleggsreferanserTilSøknad(engangstønad);

        assertThat(engangstønad.barn().getAlleVedlegg()).containsExactly(vedleggBarn.getId());
        assertThat(engangstønad.tilrettelegging().stream().filter(t -> t.type().equals(TilretteleggingDto.Type.HEL)).findFirst().orElseThrow().vedlegg())
            .containsExactly(vedleggPrivat.getId());
        assertThat(engangstønad.tilrettelegging().stream().filter(t -> t.type().equals(TilretteleggingDto.Type.DELVIS)).findFirst().orElseThrow().vedlegg())
            .containsExactly(vedleggVirksomhet.getId());
        assertThat(engangstønad.tilrettelegging().stream().filter(t -> t.type().equals(TilretteleggingDto.Type.INGEN)).findFirst().orElseThrow().vedlegg())
            .containsExactly(vedleggNæring.getId());
    }

    private static VedleggDto vedlegg(VedleggDto.Dokumenterer dokumenterer) {
        return new VedleggDto(
            null,
            null,
            null,
            LASTET_OPP.name(),
            "I0000052",
            UUID.randomUUID().toString(),
            null,
            dokumenterer
        );
    }
}
