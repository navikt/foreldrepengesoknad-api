package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static no.nav.foreldrepenger.common.util.ResourceHandleUtil.bytesFra;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.common.mapper.DefaultJsonMapper;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.EttersendelseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.YtelseType;


class EttersendelseDtoDeseraliseringTest {

    private static final ObjectMapper MAPPER = DefaultJsonMapper.MAPPER;

    @Test
    void ettersendelseSeraliseringVirkerTest() throws IOException {
        var ettersendelse = MAPPER.readValue(bytesFra("json/ettersendelse_I000044.json"), EttersendelseDto.class);

        assertThat(ettersendelse.type()).isEqualTo(YtelseType.FORELDREPENGER);
        assertThat(ettersendelse.saksnummer().value()).isEqualTo("352003201");
        assertThat(ettersendelse.vedlegg()).hasSize(1);
        var vedlegg = ettersendelse.vedlegg().get(0);
        assertThat(vedlegg.getId()).isEqualTo(new VedleggDto.Referanse("V090740687265315217194125674862219730"));
        assertThat(vedlegg.getSkjemanummer()).isEqualTo("I000044");
        assertThat(vedlegg.getContent()).isNull();
        assertThat(vedlegg.getInnsendingsType()).isNull();
    }
}
