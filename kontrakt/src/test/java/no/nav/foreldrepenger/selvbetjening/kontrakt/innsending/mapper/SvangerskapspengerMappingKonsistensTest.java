package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.TilretteleggingBuilder.delvis;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.TilretteleggingBuilder.hel;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.TilretteleggingBuilder.ingen;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler.MedlemsskapMaler.medlemskapUtlandetForrige12mnd;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Orgnummer;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.Svangerskapspenger;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.DelvisTilrettelegging;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.HelTilrettelegging;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.IngenTilrettelegging;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.Tilrettelegging;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.arbeidsforhold.PrivatArbeidsgiver;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.arbeidsforhold.SelvstendigNæringsdrivende;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.arbeidsforhold.Virksomhet;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.BarnBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.SvangerskapspengerBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.SøkerBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler.ArbeidsforholdMaler;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler.OpptjeningMaler;

class SvangerskapspengerMappingKonsistensTest {
    private static final LocalDate NOW = LocalDate.now();
    private static final Fødselsnummer DUMMY_FNR = new Fødselsnummer("0000000000");

    @Test
    void svangerskapspengerTilretteleggingKonsistensSjekk() {
        var tilretteleggingerDto = List.of(
            hel(NOW.minusMonths(1), NOW.minusMonths(1), ArbeidsforholdMaler.selvstendigNæringsdrivende()).build(),
            delvis(NOW, NOW, ArbeidsforholdMaler.privatArbeidsgiver(DUMMY_FNR.value()), 55.0).build(),
            ingen(NOW.plusWeeks(1), NOW.plusWeeks(1), ArbeidsforholdMaler.virksomhet(Orgnummer.MAGIC_ORG)).build()
        );
        var søknadDto = new SvangerskapspengerBuilder(tilretteleggingerDto)
            .medMedlemsskap(medlemskapUtlandetForrige12mnd())
            .medSøker(new SøkerBuilder(BrukerRolle.MOR)
                .medSpråkkode(Målform.EN)
                .medSelvstendigNæringsdrivendeInformasjon(List.of(OpptjeningMaler.egenNaeringOpptjening(Orgnummer.MAGIC_ORG.value())))
                .build())
            .medBarn(BarnBuilder.termin(2, LocalDate.now().plusWeeks(2)).build())
            .build();

        // Act
        var mappedSøknad = SøknadMapper.tilSøknad(søknadDto, NOW);

        // Assert
        assertThat(mappedSøknad.getSøker().søknadsRolle()).isEqualTo(søknadDto.søker().rolle());
        assertThat(mappedSøknad.getSøker().målform()).isEqualTo(søknadDto.søker().språkkode());
        assertThat(mappedSøknad.getMottattdato()).isEqualTo(søknadDto.mottattdato());
        assertThat(mappedSøknad.getTilleggsopplysninger()).isNull();

        var ytelse = mappedSøknad.getYtelse();
        assertThat(ytelse).isInstanceOf(Svangerskapspenger.class);
        var tilrettelegginger = ((Svangerskapspenger) ytelse).tilrettelegging();
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


    }
}
