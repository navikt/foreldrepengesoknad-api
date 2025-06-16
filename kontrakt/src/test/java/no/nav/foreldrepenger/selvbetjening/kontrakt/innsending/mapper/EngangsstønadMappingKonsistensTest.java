package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler.UtenlandsoppholdMaler.oppholdIUtlandetForrige12mnd;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.common.domain.engangsstønad.Engangsstønad;
import no.nav.foreldrepenger.common.domain.felles.VedleggReferanse;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Fødsel;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.FødselDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.BarnBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.EngangsstønadBuilder;

class EngangsstønadMappingKonsistensTest {
    private static final LocalDate NOW = LocalDate.now();

    @Test
    void engangsstønadV2Konsisens() {
        var søknadDto = new EngangsstønadBuilder().medSpråkkode(Målform.EN)
            .medUtenlandsopphold(oppholdIUtlandetForrige12mnd())
            .medBarn(BarnBuilder.fødsel(1, NOW).build())
            .build();

        var mappedSøknad = SøknadMapper.tilSøknad(søknadDto, NOW);
        assertThat(mappedSøknad.getSøker().målform()).isEqualTo(søknadDto.språkkode());
        assertThat(mappedSøknad.getMottattdato()).isEqualTo(søknadDto.mottattdato());

        var ytelse = mappedSøknad.getYtelse();
        assertThat(ytelse).isInstanceOf(Engangsstønad.class);
        var engangsstønad = (Engangsstønad) ytelse;

        // Medlemsskap
        assertThat(engangsstønad.utenlandsopphold().landVedDato(LocalDate.now().minusMonths(2))).isNotEqualByComparingTo(CountryCode.NO);
        assertThat(engangsstønad.utenlandsopphold().opphold()).hasSize(1);

        // Barn
        var barnDto = søknadDto.barn();
        var relasjonTilBarn = engangsstønad.relasjonTilBarn();
        assertThat(relasjonTilBarn.getAntallBarn()).isEqualTo(barnDto.antallBarn());
        assertThat(relasjonTilBarn).isInstanceOf(Fødsel.class);
        var fødsel = ((Fødsel) relasjonTilBarn);
        var fødselDto = ((FødselDto) barnDto);
        assertThat(relasjonTilBarn.relasjonsDato()).isEqualTo(fødselDto.fødselsdato());
        assertThat(fødsel.getTermindato()).isEqualTo(fødselDto.termindato());
    }

    @Test
    void engangsstønadV2VedleggReferanseKonsistensTest() {
        var vedlegg1 = DokumentasjonUtil.vedlegg(DokumentasjonUtil.barn());
        var søknadDto = new EngangsstønadBuilder().medSpråkkode(Målform.EN)
            .medUtenlandsopphold(oppholdIUtlandetForrige12mnd())
            .medBarn(BarnBuilder.fødsel(1, NOW).build())
            .medVedlegg(List.of(vedlegg1))
            .build();

        var mappedSøknad = SøknadMapper.tilSøknad(søknadDto, NOW);
        var engangsstønad = (Engangsstønad) mappedSøknad.getYtelse();

        assertThat(mappedSøknad.getVedlegg()).hasSameSizeAs(søknadDto.vedlegg());
        assertThat(vedlegg1.referanse().verdi()).isNotNull();
        assertThat(engangsstønad.relasjonTilBarn().getVedlegg()).extracting(VedleggReferanse::referanse)
            .containsExactly(vedlegg1.referanse().verdi());
    }
}
