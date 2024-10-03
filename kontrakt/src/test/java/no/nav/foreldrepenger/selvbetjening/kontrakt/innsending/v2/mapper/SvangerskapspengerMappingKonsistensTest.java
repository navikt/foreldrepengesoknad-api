package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper;

import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.TilretteleggingBuilder.delvis;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.TilretteleggingBuilder.hel;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.TilretteleggingBuilder.ingen;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Orgnummer;
import no.nav.foreldrepenger.common.domain.felles.VedleggReferanse;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.Svangerskapspenger;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.DelvisTilrettelegging;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.HelTilrettelegging;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.IngenTilrettelegging;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.Tilrettelegging;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.arbeidsforhold.PrivatArbeidsgiver;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.arbeidsforhold.SelvstendigNæringsdrivende;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.arbeidsforhold.Virksomhet;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilretteleggingsbehov.Tilretteleggingbehov;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.DokumentasjonUtil;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.SøknadMapper;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.AvtaltFerieDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.tilretteleggingbehov.TilretteleggingbehovDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.BarnBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.SvangerskapspengerBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder.TilretteleggingBehovBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.maler.ArbeidsforholdMaler;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.maler.OpptjeningMaler;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.maler.UtenlandsoppholdMaler;

class SvangerskapspengerMappingKonsistensTest {
    private static final LocalDate NOW = LocalDate.now();
    private static final Fødselsnummer DUMMY_FNR = new Fødselsnummer("0000000000");

    @Test
    void svangerskapspengerTilretteleggingKonsistensSjekk() {
        var tilretteleggingerDto = List.of(
            hel(NOW.minusMonths(1), NOW.minusMonths(1), ArbeidsforholdMaler.selvstendigNæringsdrivende()).build(),
            delvis(NOW, NOW, ArbeidsforholdMaler.privatArbeidsgiver(DUMMY_FNR), 55.0).build(),
            ingen(NOW.plusWeeks(1), NOW.plusWeeks(1), ArbeidsforholdMaler.virksomhet(Orgnummer.MAGIC_ORG)).build()
        );
        var ferie = new AvtaltFerieDto(ArbeidsforholdMaler.virksomhet(Orgnummer.MAGIC_ORG), LocalDate.now().plusDays(10),
            LocalDate.now().plusDays(20));
        var søknadDto = new SvangerskapspengerBuilder(null)
            .medTilrettelegging(tilretteleggingerDto)
            .medUtenlandsopphold(UtenlandsoppholdMaler.oppholdIUtlandetForrige12mnd())
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
        var tilrettelegginger = svpMappet.tilrettelegging();
        assertThat(tilrettelegginger).hasSameSizeAs(tilretteleggingerDto)
            .hasExactlyElementsOfTypes(
                HelTilrettelegging.class,
                DelvisTilrettelegging.class,
                IngenTilrettelegging.class
            )
            .extracting(Tilrettelegging::getBehovForTilretteleggingFom)
            .containsExactly(
                tilretteleggingerDto.get(0).behovForTilretteleggingFom(),
                tilretteleggingerDto.get(1).behovForTilretteleggingFom(),
                tilretteleggingerDto.get(2).behovForTilretteleggingFom()
            );
        assertThat(tilrettelegginger)
            .extracting(Tilrettelegging::getArbeidsforhold)
            .hasExactlyElementsOfTypes(
                SelvstendigNæringsdrivende.class,
                PrivatArbeidsgiver.class,
                Virksomhet.class
            );
        assertThat(svpMappet.avtaltFerie()).hasSize(1)
            .first().satisfies(af -> {
                assertThat(af.arbeidsforhold()).isInstanceOf(Virksomhet.class);
                assertThat(af.ferieFom()).isEqualTo(LocalDate.now().plusDays(10));
                assertThat(af.ferieTom()).isEqualTo(LocalDate.now().plusDays(20));
            });
    }

    @Test
    void svangerskapspengerTilretteleggingBehovKonsistensSjekk() {
        var tilretteleggingBehovDto = List.of(
            new TilretteleggingbehovDto(ArbeidsforholdMaler.selvstendigNæringsdrivende(), NOW.minusMonths(1), List.of(
                TilretteleggingBehovBuilder.hel(NOW.minusMonths(1)),
                TilretteleggingBehovBuilder.delvis(NOW, 55.0))
            ),
            new TilretteleggingbehovDto(ArbeidsforholdMaler.privatArbeidsgiver(DUMMY_FNR), NOW.minusMonths(1), List.of(
                TilretteleggingBehovBuilder.delvis(NOW, 55.0))),
            new TilretteleggingbehovDto(ArbeidsforholdMaler.virksomhet(Orgnummer.MAGIC_ORG), NOW.minusMonths(1), List.of(
                TilretteleggingBehovBuilder.ingen(NOW.plusMonths(1))))
        );
        var ferie = new AvtaltFerieDto(ArbeidsforholdMaler.virksomhet(Orgnummer.MAGIC_ORG), LocalDate.now().plusDays(10),
            LocalDate.now().plusDays(20));
        var søknadDto = new SvangerskapspengerBuilder(tilretteleggingBehovDto)
            .medUtenlandsopphold(UtenlandsoppholdMaler.oppholdIUtlandetForrige12mnd())
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
            .containsExactly(
                tilretteleggingBehovDto.get(0).behovForTilretteleggingFom(),
                tilretteleggingBehovDto.get(1).behovForTilretteleggingFom(),
                tilretteleggingBehovDto.get(2).behovForTilretteleggingFom()
            );
        assertThat(tilretteleggingbehov).hasSameSizeAs(tilretteleggingBehovDto)
            .flatExtracting(Tilretteleggingbehov::tilrettelegginger)
            .hasExactlyElementsOfTypes(
                Tilretteleggingbehov.HelTilrettelegging.class,
                Tilretteleggingbehov.DelvisTilrettelegging.class,
                Tilretteleggingbehov.DelvisTilrettelegging.class,
                Tilretteleggingbehov.IngenTilrettelegging.class
            );
        assertThat(tilretteleggingbehov)
            .extracting(Tilretteleggingbehov::arbeidsforhold)
            .hasExactlyElementsOfTypes(
                SelvstendigNæringsdrivende.class,
                PrivatArbeidsgiver.class,
                Virksomhet.class
            );
        assertThat(svpMappet.avtaltFerie()).hasSize(1)
            .first().satisfies(af -> {
                assertThat(af.arbeidsforhold()).isInstanceOf(Virksomhet.class);
                assertThat(af.ferieFom()).isEqualTo(LocalDate.now().plusDays(10));
                assertThat(af.ferieTom()).isEqualTo(LocalDate.now().plusDays(20));
            });
    }

    @Test
    void svangerskapspengerVedleggReferanseMappingKonsistens() {
        var tilretteleggingerDto = List.of(
            hel(NOW.minusMonths(1), NOW.minusMonths(1), ArbeidsforholdMaler.selvstendigNæringsdrivende()).build(),
            delvis(NOW, NOW, ArbeidsforholdMaler.privatArbeidsgiver(DUMMY_FNR), 55.0).build(),
            ingen(NOW.plusWeeks(1), NOW.plusWeeks(1), ArbeidsforholdMaler.virksomhet(Orgnummer.MAGIC_ORG)).build()
        );
        var vedlegg1 = DokumentasjonUtil.vedlegg(DokumentasjonUtil.tilrettelegging(tilretteleggingerDto.get(0).arbeidsforhold()));
        var vedlegg2 = DokumentasjonUtil.vedlegg(DokumentasjonUtil.tilrettelegging(tilretteleggingerDto.get(1).arbeidsforhold()));
        var vedlegg3 = DokumentasjonUtil.vedlegg(DokumentasjonUtil.tilrettelegging(tilretteleggingerDto.get(2).arbeidsforhold()));
        var søknadDto = new SvangerskapspengerBuilder(null)
            .medTilrettelegging(tilretteleggingerDto)
            .medSpråkkode(Målform.EN)
            .medUtenlandsopphold(UtenlandsoppholdMaler.oppholdIUtlandetForrige12mnd())
            .medSelvstendigNæringsdrivendeInformasjon(OpptjeningMaler.egenNaeringOpptjening(Orgnummer.MAGIC_ORG.value()))
            .medBarn(BarnBuilder.termin(2, LocalDate.now().plusWeeks(2)).build())
            .medVedlegg(List.of(vedlegg1, vedlegg2, vedlegg3))
            .build();

        var mappedSøknad = SøknadMapper.tilSøknad(søknadDto, NOW);
        var svp = (Svangerskapspenger) mappedSøknad.getYtelse();

        assertThat(mappedSøknad.getVedlegg()).hasSameSizeAs(søknadDto.vedlegg());
        assertThat(svp.tilrettelegging().get(0).getVedlegg()).extracting(VedleggReferanse::referanse)
            .containsExactly(vedlegg1.referanse().verdi());
        assertThat(svp.tilrettelegging().get(1).getVedlegg()).extracting(VedleggReferanse::referanse)
            .containsExactly(vedlegg2.referanse().verdi());
        assertThat(svp.tilrettelegging().get(2).getVedlegg()).extracting(VedleggReferanse::referanse)
            .containsExactly(vedlegg3.referanse().verdi());
    }

    @Test
    void svangerskapspengerBehovVedleggReferanseMappingKonsistens() {
        var tilretteleggingBehov = List.of(
            new TilretteleggingbehovDto(ArbeidsforholdMaler.selvstendigNæringsdrivende(), NOW.minusMonths(1), List.of(
                TilretteleggingBehovBuilder.hel(NOW.minusMonths(1)))),
            new TilretteleggingbehovDto(ArbeidsforholdMaler.privatArbeidsgiver(DUMMY_FNR), NOW.minusMonths(1), List.of(
                TilretteleggingBehovBuilder.delvis(NOW, 55.0))),
            new TilretteleggingbehovDto(ArbeidsforholdMaler.virksomhet(Orgnummer.MAGIC_ORG), NOW.minusMonths(1), List.of(
                TilretteleggingBehovBuilder.ingen(NOW.plusMonths(1))))
        );
        var vedlegg1 = DokumentasjonUtil.vedlegg(DokumentasjonUtil.tilrettelegging(tilretteleggingBehov.get(0).arbeidsforhold()));
        var vedlegg2 = DokumentasjonUtil.vedlegg(DokumentasjonUtil.tilrettelegging(tilretteleggingBehov.get(1).arbeidsforhold()));
        var vedlegg3 = DokumentasjonUtil.vedlegg(DokumentasjonUtil.tilrettelegging(tilretteleggingBehov.get(2).arbeidsforhold()));
        var søknadDto = new SvangerskapspengerBuilder(tilretteleggingBehov)
            .medSpråkkode(Målform.EN)
            .medUtenlandsopphold(UtenlandsoppholdMaler.oppholdIUtlandetForrige12mnd())
            .medSelvstendigNæringsdrivendeInformasjon(OpptjeningMaler.egenNaeringOpptjening(Orgnummer.MAGIC_ORG.value()))
            .medBarn(BarnBuilder.termin(2, LocalDate.now().plusWeeks(2)).build())
            .medVedlegg(List.of(vedlegg1, vedlegg2, vedlegg3))
            .build();

        var mappedSøknad = SøknadMapper.tilSøknad(søknadDto, NOW);
        var svp = (Svangerskapspenger) mappedSøknad.getYtelse();

        assertThat(mappedSøknad.getVedlegg()).hasSameSizeAs(søknadDto.vedlegg());
        assertThat(svp.tilretteleggingbehov().get(0).vedlegg()).extracting(VedleggReferanse::referanse)
            .containsExactly(vedlegg1.referanse().verdi());
        assertThat(svp.tilretteleggingbehov().get(1).vedlegg()).extracting(VedleggReferanse::referanse)
            .containsExactly(vedlegg2.referanse().verdi());
        assertThat(svp.tilretteleggingbehov().get(2).vedlegg()).extracting(VedleggReferanse::referanse)
            .containsExactly(vedlegg3.referanse().verdi());
    }
}
