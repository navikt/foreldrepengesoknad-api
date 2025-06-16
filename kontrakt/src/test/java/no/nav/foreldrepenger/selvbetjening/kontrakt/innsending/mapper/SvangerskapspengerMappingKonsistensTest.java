package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Orgnummer;
import no.nav.foreldrepenger.common.domain.felles.VedleggReferanse;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.Svangerskapspenger;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.Tilretteleggingbehov;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.arbeidsforhold.PrivatArbeidsgiver;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.arbeidsforhold.SelvstendigNæringsdrivende;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.arbeidsforhold.Virksomhet;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.AvtaltFerieDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.BarnBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.SvangerskapspengerBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.TilretteleggingBehovBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler.ArbeidsforholdMaler;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler.OpptjeningMaler;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler.UtenlandsoppholdMaler;

class SvangerskapspengerMappingKonsistensTest {
    private static final LocalDate NOW = LocalDate.now();
    private static final Fødselsnummer DUMMY_FNR = new Fødselsnummer("0000000000");

    @Test
    void svangerskapspengerTilretteleggingBehovKonsistensSjekk() {
        var tilretteleggingBehovDto = List.of(new TilretteleggingBehovBuilder(ArbeidsforholdMaler.selvstendigNæringsdrivende(),
                NOW.minusMonths(1)).hel(NOW.minusMonths(1)).delvis(NOW, 55.0).build(),
            new TilretteleggingBehovBuilder(ArbeidsforholdMaler.privatArbeidsgiver(DUMMY_FNR), NOW).delvis(NOW, 55.0).build(),
            new TilretteleggingBehovBuilder(ArbeidsforholdMaler.virksomhet(Orgnummer.MAGIC_ORG), NOW.plusMonths(1)).ingen(NOW.plusMonths(1)).build());


        var ferie = new AvtaltFerieDto(ArbeidsforholdMaler.virksomhet(Orgnummer.MAGIC_ORG),
            LocalDate.now().plusDays(10),
            LocalDate.now().plusDays(20));
        var søknadDto =
            new SvangerskapspengerBuilder(tilretteleggingBehovDto).medUtenlandsopphold(UtenlandsoppholdMaler.oppholdIUtlandetForrige12mnd())
            .medSpråkkode(Målform.EN)
            .medAvtaltFerie(List.of(ferie))
            .medSelvstendigNæringsdrivendeInformasjon(OpptjeningMaler.egenNaeringOpptjening(Orgnummer.MAGIC_ORG.value()))
            .medBarn(BarnBuilder.termin(2, LocalDate.now().plusWeeks(2)).build())
            .build();

        // Act
        var mappedSøknad = SøknadMapper.tilSøknad(søknadDto, NOW);

        // Assert
        assertThat(mappedSøknad.getSøker().søknadsRolle()).isEqualTo(BrukerRolle.MOR);
        assertThat(mappedSøknad.getSøker().målform()).isEqualTo(søknadDto.språkkode());
        assertThat(mappedSøknad.getMottattdato()).isEqualTo(søknadDto.mottattdato());
        assertThat(mappedSøknad.getTilleggsopplysninger()).isNull();

        assertThat(mappedSøknad.getYtelse()).isInstanceOf(Svangerskapspenger.class);
        var svpMappet = (Svangerskapspenger) mappedSøknad.getYtelse();
        var tilretteleggingbehov = svpMappet.tilretteleggingbehov();
        assertThat(tilretteleggingbehov).hasSameSizeAs(tilretteleggingBehovDto)
            .extracting(Tilretteleggingbehov::behovForTilretteleggingFom)
            .containsExactly(tilretteleggingBehovDto.get(0).behovForTilretteleggingFom(),
                tilretteleggingBehovDto.get(1).behovForTilretteleggingFom(),
                tilretteleggingBehovDto.get(2).behovForTilretteleggingFom());
        assertThat(tilretteleggingbehov).hasSameSizeAs(tilretteleggingBehovDto)
            .flatExtracting(Tilretteleggingbehov::tilrettelegginger)
            .hasExactlyElementsOfTypes(Tilretteleggingbehov.Tilrettelegging.Hel.class,
                Tilretteleggingbehov.Tilrettelegging.Delvis.class,
                Tilretteleggingbehov.Tilrettelegging.Delvis.class,
                Tilretteleggingbehov.Tilrettelegging.Ingen.class);
        assertThat(tilretteleggingbehov).extracting(Tilretteleggingbehov::arbeidsforhold)
            .hasExactlyElementsOfTypes(SelvstendigNæringsdrivende.class, PrivatArbeidsgiver.class, Virksomhet.class);
        assertThat(svpMappet.avtaltFerie()).hasSize(1).first().satisfies(af -> {
            assertThat(af.arbeidsforhold()).isInstanceOf(Virksomhet.class);
            assertThat(af.ferieFom()).isEqualTo(LocalDate.now().plusDays(10));
            assertThat(af.ferieTom()).isEqualTo(LocalDate.now().plusDays(20));
        });
    }

    @Test
    void svangerskapspengerBehovVedleggReferanseMappingKonsistens() {
        var tilretteleggingbehov = List.of(new TilretteleggingBehovBuilder(ArbeidsforholdMaler.selvstendigNæringsdrivende(), NOW.minusMonths(1)).hel(
                NOW.minusMonths(1)).build(),
            new TilretteleggingBehovBuilder(ArbeidsforholdMaler.privatArbeidsgiver(DUMMY_FNR), NOW).delvis(NOW, 55.0).build(),
            new TilretteleggingBehovBuilder(ArbeidsforholdMaler.virksomhet(Orgnummer.MAGIC_ORG), NOW.plusMonths(1)).ingen(NOW.plusMonths(1)).build());

        var vedlegg1 = DokumentasjonUtil.vedlegg(DokumentasjonUtil.tilrettelegging(tilretteleggingbehov.get(0).arbeidsforhold()));
        var vedlegg2 = DokumentasjonUtil.vedlegg(DokumentasjonUtil.tilrettelegging(tilretteleggingbehov.get(1).arbeidsforhold()));
        var vedlegg3 = DokumentasjonUtil.vedlegg(DokumentasjonUtil.tilrettelegging(tilretteleggingbehov.get(2).arbeidsforhold()));
        var søknadDto = new SvangerskapspengerBuilder(tilretteleggingbehov).medSpråkkode(Målform.EN)
            .medUtenlandsopphold(UtenlandsoppholdMaler.oppholdIUtlandetForrige12mnd())
            .medSelvstendigNæringsdrivendeInformasjon(OpptjeningMaler.egenNaeringOpptjening(Orgnummer.MAGIC_ORG.value()))
            .medBarn(BarnBuilder.termin(2, LocalDate.now().plusWeeks(2)).build())
            .medVedlegg(List.of(vedlegg1, vedlegg2, vedlegg3))
            .build();

        var mappedSøknad = SøknadMapper.tilSøknad(søknadDto, NOW);
        var svp = (Svangerskapspenger) mappedSøknad.getYtelse();

        assertThat(mappedSøknad.getVedlegg()).hasSameSizeAs(søknadDto.vedlegg());
        assertThat(svp.tilretteleggingbehov().get(0).vedlegg()).extracting(VedleggReferanse::referanse).containsExactly(vedlegg1.referanse().verdi());
        assertThat(svp.tilretteleggingbehov().get(1).vedlegg()).extracting(VedleggReferanse::referanse).containsExactly(vedlegg2.referanse().verdi());
        assertThat(svp.tilretteleggingbehov().get(2).vedlegg()).extracting(VedleggReferanse::referanse).containsExactly(vedlegg3.referanse().verdi());
    }
}
