package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler.MedlemsskapMaler.medlemskapUtlandetForrige12mnd;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.engangsstønad.Engangsstønad;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Fødsel;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.BarnBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.EngangsstønadBuilder;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder.SøkerBuilder;

public class EngangsstønadMappingKonsistensTest {
    private static final LocalDate NOW = LocalDate.now();

    @Test
    void engangsstønadKonsisens() {
        var søknadDto = new EngangsstønadBuilder()
            .medSøker(new SøkerBuilder(BrukerRolle.MOR).build())
            .medMedlemsskap(medlemskapUtlandetForrige12mnd())
            .medBarn(BarnBuilder.fødsel(1, NOW).build())
            .build();

        var mappedSøknad = SøknadMapper.tilSøknad(søknadDto, NOW);
        assertThat(mappedSøknad.getSøker().søknadsRolle()).isEqualTo(søknadDto.søker().rolle());
        assertThat(mappedSøknad.getSøker().målform()).isEqualTo(søknadDto.søker().språkkode());
        assertThat(mappedSøknad.getMottattdato()).isEqualTo(søknadDto.mottattdato());

        var ytelse = mappedSøknad.getYtelse();
        assertThat(ytelse).isInstanceOf(Engangsstønad.class);
        var engangsstønad = (Engangsstønad) ytelse;

        // Medlemsskap
        assertThat(engangsstønad.medlemsskap().isBoddINorge()).isFalse();
        assertThat(engangsstønad.medlemsskap().isNorgeNeste12()).isTrue();

        // Barn
        var barnDto = søknadDto.barn();
        var relasjonTilBarn = engangsstønad.relasjonTilBarn();
        assertThat(relasjonTilBarn.getAntallBarn()).isEqualTo(barnDto.antallBarn());
        assertThat(relasjonTilBarn.relasjonsDato()).isEqualTo(barnDto.fødselsdatoer().get(0));
        assertThat(relasjonTilBarn).isInstanceOf(Fødsel.class);
        var fødsel = ((Fødsel) relasjonTilBarn);
        assertThat(fødsel.getTermindato()).isEqualTo(barnDto.termindato());

    }
}
