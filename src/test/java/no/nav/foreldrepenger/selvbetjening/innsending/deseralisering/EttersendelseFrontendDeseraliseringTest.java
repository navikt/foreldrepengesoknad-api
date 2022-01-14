package no.nav.foreldrepenger.selvbetjening.innsending.deseralisering;

import static no.nav.foreldrepenger.common.util.ResourceHandleUtil.bytesFra;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JacksonConfiguration.class)
class EttersendelseFrontendDeseraliseringTest {

    @Autowired
    private ObjectMapper mapper;

    @Test
    void ettersendelseSeraliseringVirkerTest() throws IOException {
        var ettersendelse = mapper.readValue(bytesFra("json/ettersendelse_I000044.json"), no.nav.foreldrepenger.selvbetjening.innsending.domain.Ettersending.class);

        assertThat(ettersendelse.type()).isEqualTo("foreldrepenger");
        assertThat(ettersendelse.saksnummer()).isEqualTo("352003201");
        assertThat(ettersendelse.vedlegg()).hasSize(1);
        var vedlegg = ettersendelse.vedlegg().get(0);
        assertThat(vedlegg.getId()).isEqualTo("V090740687265315217194125674862219730");
        assertThat(vedlegg.getSkjemanummer()).isEqualTo("I000044");
        assertThat(vedlegg.getContent()).isNull();
        assertThat(vedlegg.getInnsendingsType()).isNull();
    }
}
