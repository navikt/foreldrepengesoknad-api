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

import no.nav.foreldrepenger.common.domain.felles.DokumentType;
import no.nav.foreldrepenger.common.domain.felles.EttersendingsType;
import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.innsending.InnsendingConnection;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Ettersending;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { JacksonConfiguration.class, ForeldrepengeSøknadTilDtoMapperTest.InnsendingConnectionConfiguration.class})
class EttersendingTilDtoMapperTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private InnsendingConnection connection;

    @Test
    void testEttersendingMapper() {
        var type = "foreldrepenger";
        var saksnummer = "952003131";
        var ettersendelseFraFrontend = new Ettersending(type, saksnummer, null, null, null);
        var ettersendelse = connection.body(ettersendelseFraFrontend);
        assertThat(ettersendelse.getType().name()).isEqualTo(type);
        assertThat(ettersendelse.getSaksnr()).isEqualTo(saksnummer);
        assertThat(ettersendelse.getVedlegg()).isEmpty();
    }

    @Test
    void ettersendelseSeraliseringDeseraliseringTest() throws IOException {
        var ettersendelseFraFrontend = mapper.readValue(bytesFra("json/ettersendelse_I000044.json"), no.nav.foreldrepenger.selvbetjening.innsending.domain.Ettersending.class);
        var ettersendelse = connection.body(ettersendelseFraFrontend);

        assertThat(ettersendelse.getType()).isEqualTo(EttersendingsType.foreldrepenger);
        assertThat(ettersendelse.getSaksnr()).isEqualTo("352003201");
        assertThat(ettersendelse.getVedlegg()).hasSize(1);
        var vedlegg = ettersendelse.getVedlegg().get(0);
        assertThat(vedlegg.getId()).isEqualTo("V090740687265315217194125674862219730");
        assertThat(vedlegg.getDokumentType()).isEqualTo(DokumentType.I000044);
    }
}
