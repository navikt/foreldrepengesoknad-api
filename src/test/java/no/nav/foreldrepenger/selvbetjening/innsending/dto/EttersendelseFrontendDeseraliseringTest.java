package no.nav.foreldrepenger.selvbetjening.innsending.dto;

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
import no.nav.foreldrepenger.selvbetjening.innsending.dto.ettersendelse.EttersendelseDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.ettersendelse.YtelseType;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JacksonConfiguration.class)
class EttersendelseFrontendDeseraliseringTest {

    @Autowired
    private ObjectMapper mapper;

    @Test
    void ettersendelseSeraliseringVirkerTest() throws IOException {
        var ettersendelse = mapper.readValue(bytesFra("json/ettersendelse_I000044.json"), EttersendelseDto.class);

        assertThat(ettersendelse.type()).isEqualTo(YtelseType.FORELDREPENGER);
        assertThat(ettersendelse.saksnummer().value()).isEqualTo("352003201");
        assertThat(ettersendelse.vedlegg()).hasSize(1);
        var vedlegg = ettersendelse.vedlegg().get(0);
        assertThat(vedlegg.getId()).isEqualTo(new MutableVedleggReferanseDto("V090740687265315217194125674862219730"));
        assertThat(vedlegg.getSkjemanummer()).isEqualTo("I000044");
        assertThat(vedlegg.getContent()).isNull();
        assertThat(vedlegg.getInnsendingsType()).isNull();
    }
}
