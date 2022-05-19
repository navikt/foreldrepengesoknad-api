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
import no.nav.foreldrepenger.selvbetjening.innsending.domain.EttersendingFrontend;
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JacksonConfiguration.class)
class EttersendingTilDtoMapperTest {

    private final InnsendingConnection connection = new InnsendingConnection(null, null, new Image2PDFConverter());

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testEttersendingMapper() {
        var type = "foreldrepenger";
        var saksnummer = "952003131";
        var ettersendelseFraFrontend = new EttersendingFrontend(type, saksnummer, null, null, null);
        var ettersendelse = connection.body(ettersendelseFraFrontend);
        assertThat(ettersendelse.type().name()).isEqualTo(type);
        assertThat(ettersendelse.saksnr()).isEqualTo(saksnummer);
        assertThat(ettersendelse.vedlegg()).isEmpty();
    }

    @Test
    void ettersendelseSeraliseringDeseraliseringTest() throws IOException {
        var ettersendelseFraFrontend = mapper.readValue(bytesFra("json/ettersendelse_I000044.json"), EttersendingFrontend.class);
        var ettersendelse = connection.body(ettersendelseFraFrontend);

        assertThat(ettersendelse.type()).isEqualTo(EttersendingsType.foreldrepenger);
        assertThat(ettersendelse.saksnr()).isEqualTo("352003201");
        assertThat(ettersendelse.vedlegg()).hasSize(1);
        var vedlegg = ettersendelse.vedlegg().get(0);
        assertThat(vedlegg.getId()).isEqualTo("V090740687265315217194125674862219730");
        assertThat(vedlegg.getDokumentType()).isEqualTo(DokumentType.I000044);
    }
}
