package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintViolationException;
import no.nav.foreldrepenger.common.util.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.error.ApiExceptionHandler;
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;

@Import({MellomlagringController.class, ApiExceptionHandler.class})
@WebMvcTest(controllers = MellomlagringController.class)
@ContextConfiguration(classes = {
    JacksonConfiguration.class,
    Image2PDFConverter.class
})
class MellomlagringControllerInputValideringTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private KryptertMellomlagring kryptertMellomlagring;

    @MockBean
    private TokenUtil tokenUtil;

    @Test
    void hentVedleggOkMedUUID() throws Exception {
        var key = UUID.randomUUID();
        var result = mvc.perform(get(MellomlagringController.REST_STORAGE + "/vedlegg/{key}", key)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andReturn();
        assertThat(result.getResolvedException()).isNull();
    }

    @Test
    void hentVedleggHiverExceptionMedUlovligKey() throws Exception {
        var key = "<Special=key∁\uD835\uDD4A>";
        var result = mvc.perform(get(MellomlagringController.REST_STORAGE + "/vedlegg/{key}", key)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();

        assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class);
    }

}
