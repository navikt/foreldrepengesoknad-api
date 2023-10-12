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
import no.nav.foreldrepenger.common.domain.svangerskapspenger.Svangerskapspenger;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.DelvisTilrettelegging;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.arbeidsforhold.Virksomhet;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.innsending.InnsendingConnection;
import no.nav.foreldrepenger.selvbetjening.innsending.VedleggsHåndteringTjeneste;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.svangerskapspenger.SvangerskapspengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JacksonConfiguration.class)
class SvangerskapspengesøknadTilDtoMapperTest {

    private final InnsendingConnection connection = new InnsendingConnection(null, null, new VedleggsHåndteringTjeneste(new Image2PDFConverter()));

    @Autowired
    private ObjectMapper mapper;


    @Test
    void SvangerskapspengerSøknadMapperTest() throws IOException {
        var sf = mapper.readValue(bytesFra("json/svangerskapspengesøknad.json"), SøknadDto.class);
        assertThat(sf).isNotNull().isInstanceOf(SvangerskapspengesøknadDto.class);

        var søknad = connection.body(sf);
        assertThat(søknad.getYtelse()).isInstanceOf(Svangerskapspenger.class);
        var ytelse = (Svangerskapspenger) søknad.getYtelse();
        assertThat(ytelse.fødselsdato()).isNull();
        assertThat(ytelse.termindato()).isNotNull();

        var søker = søknad.getSøker();
        assertThat(søker.søknadsRolle()).isEqualTo(BrukerRolle.MOR);
        assertThat(søker.målform()).isEqualTo(Målform.NB);

        // Medlemsskap
        var medlemsskap = ytelse.medlemsskap();
        assertThat(medlemsskap).isNotNull();
        assertThat(medlemsskap.framtidigUtenlandsopphold()).isEmpty();
        assertThat(medlemsskap.tidligereUtenlandsopphold()).isEmpty();

        // Opptjening
        var opptjening = ytelse.opptjening();
        assertThat(opptjening.utenlandskArbeidsforhold()).isEmpty();
        assertThat(opptjening.egenNæring()).isEmpty();
        assertThat(opptjening.annenOpptjening()).isEmpty();
        assertThat(opptjening.frilans()).isNull();

        // Tilrettelegging
        var tilrettelegginger = ytelse.tilrettelegging();
        assertThat(tilrettelegginger).hasSize(1);
        assertThat(tilrettelegginger.get(0)).isInstanceOf(DelvisTilrettelegging.class);
        var tilrettelegging = (DelvisTilrettelegging) tilrettelegginger.get(0);
        assertThat(tilrettelegging.getArbeidsforhold()).isInstanceOf(Virksomhet.class);
        assertThat(tilrettelegging.getTilrettelagtArbeidFom()).isEqualTo(LocalDate.of(2021, 12, 2));
        assertThat(tilrettelegging.getBehovForTilretteleggingFom()).isEqualTo(LocalDate.of(2021, 12, 1));
        assertThat(tilrettelegging.getStillingsprosent()).isEqualTo(ProsentAndel.valueOf(50));
        assertThat(tilrettelegging.getStillingsprosent()).isEqualTo(ProsentAndel.valueOf(50));
        assertThat(tilrettelegging.getVedlegg()).hasSize(1);
    }

}
