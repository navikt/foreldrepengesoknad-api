package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import static no.nav.foreldrepenger.common.util.ResourceHandleUtil.bytesFra;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.felles.ProsentAndel;
import no.nav.foreldrepenger.common.domain.felles.medlemskap.ArbeidsInformasjon;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.Svangerskapspenger;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.DelvisTilrettelegging;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.arbeidsforhold.Virksomhet;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.innsending.InnsendingConnection;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SvangerskapspengesøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadFrontend;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { JacksonConfiguration.class, ForeldrepengeSøknadTilDtoMapperTest.InnsendingConnectionConfiguration.class})
class SvangerskapspengesøknadTilDtoMapperTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private InnsendingConnection connection;


    @Test
    void SvangerskapspengerSøknadMapperTest() throws IOException {
        var sf = mapper.readValue(bytesFra("json/svangerskapspengesøknad.json"), SøknadFrontend.class);
        assertThat(sf).isNotNull().isInstanceOf(SvangerskapspengesøknadFrontend.class);

        var søknad = connection.body(sf);
        assertThat(søknad.getYtelse()).isInstanceOf(Svangerskapspenger.class);
        var ytelse = (Svangerskapspenger) søknad.getYtelse();
        assertThat(ytelse.getFødselsdato()).isNull();
        assertThat(ytelse.getTermindato()).isNotNull();

        var søker = søknad.getSøker();
        assertThat(søker.getSøknadsRolle()).isEqualTo(BrukerRolle.MOR);
        assertThat(søker.getMålform()).isEqualTo(Målform.NB);

        // Medlemsskap
        var medlemsskap = ytelse.getMedlemsskap();
        assertThat(medlemsskap).isNotNull();
        assertThat(medlemsskap.getFramtidigOppholdsInfo()).isNotNull();
        assertThat(medlemsskap.getFramtidigOppholdsInfo().getUtenlandsOpphold()).isEmpty();
        assertThat(medlemsskap.getTidligereOppholdsInfo()).isNotNull();
        assertThat(medlemsskap.getTidligereOppholdsInfo().getUtenlandsOpphold()).isEmpty();
        assertThat(medlemsskap.getTidligereOppholdsInfo().getArbeidSiste12()).isEqualTo(ArbeidsInformasjon.IKKE_ARBEIDET);

        // Opptjening
        var opptjening = ytelse.getOpptjening();
        assertThat(opptjening.getUtenlandskArbeidsforhold()).isEmpty();
        assertThat(opptjening.getEgenNæring()).isEmpty();
        assertThat(opptjening.getAnnenOpptjening()).isEmpty();
        assertThat(opptjening.getFrilans()).isNull();

        // Tilrettelegging
        var tilrettelegginger = ytelse.getTilrettelegging();
        assertThat(tilrettelegginger).hasSize(1);
        assertThat(tilrettelegginger.get(0)).isInstanceOf(DelvisTilrettelegging.class);
        var tilrettelegging = (DelvisTilrettelegging) tilrettelegginger.get(0);
        assertThat(tilrettelegging.getArbeidsforhold()).isInstanceOf(Virksomhet.class);
        assertThat(tilrettelegging.getTilrettelagtArbeidFom()).isEqualTo(LocalDate.of(2021, 12, 2));
        assertThat(tilrettelegging.getBehovForTilretteleggingFom()).isEqualTo(LocalDate.of(2021, 12, 1));
        assertThat(tilrettelegging.getStillingsprosent()).isEqualTo(new ProsentAndel(50));
        assertThat(tilrettelegging.getStillingsprosent()).isEqualTo(new ProsentAndel(50));
        assertThat(tilrettelegging.getVedlegg()).hasSize(1);
    }

}
