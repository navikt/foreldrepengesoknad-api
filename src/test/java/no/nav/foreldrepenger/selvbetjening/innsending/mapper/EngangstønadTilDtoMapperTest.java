package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import static no.nav.foreldrepenger.common.util.ResourceHandleUtil.bytesFra;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.common.domain.engangsstønad.Engangsstønad;
import no.nav.foreldrepenger.common.domain.felles.medlemskap.ArbeidsInformasjon;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.FremtidigFødsel;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.innsending.InnsendingConnection;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.EngangsstønadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JacksonConfiguration.class)
class EngangstønadTilDtoMapperTest {

    private final InnsendingConnection connection = new InnsendingConnection(null, null, new Image2PDFConverter());

    @Autowired
    private ObjectMapper mapper;

    @Test
    void EngangsstønadMapperTest() throws IOException {
        var sf = mapper.readValue(bytesFra("json/engangsstønad.json"), SøknadFrontend.class);
        assertThat(sf).isNotNull().isInstanceOf(EngangsstønadFrontend.class);

        var søknad = connection.body(sf);

        var søker = søknad.getSøker();
        assertThat(søker.getMålform()).isEqualTo(Målform.NB);

        assertThat(søknad.getYtelse()).isInstanceOf(Engangsstønad.class);
        var ytelse = (Engangsstønad) søknad.getYtelse();

        assertThat(ytelse.getRelasjonTilBarn()).isInstanceOf(FremtidigFødsel.class);
        var barn = (FremtidigFødsel) ytelse.getRelasjonTilBarn();
        assertThat(barn.getAntallBarn()).isEqualTo(2);
        assertThat(barn.getTerminDato()).isNotNull();
        assertThat(barn.getUtstedtDato()).isNotNull();

        // Medlemsskap
        var medlemsskap = ytelse.getMedlemsskap();
        assertThat(medlemsskap).isNotNull();
        assertThat(medlemsskap.getFramtidigOppholdsInfo()).isNotNull();
        assertThat(medlemsskap.getFramtidigOppholdsInfo().getUtenlandsOpphold()).isEmpty();
        assertThat(medlemsskap.getTidligereOppholdsInfo()).isNotNull();
        assertThat(medlemsskap.getTidligereOppholdsInfo().getUtenlandsOpphold()).isEmpty();
        assertThat(medlemsskap.getTidligereOppholdsInfo().getArbeidSiste12()).isEqualTo(ArbeidsInformasjon.IKKE_ARBEIDET);
    }

}
