package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static no.nav.foreldrepenger.selvbetjening.vedlegg.DelegerendeVedleggSjekker.DELEGERENDE;
import static no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekkerTest.fraResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.EOFException;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartException;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintViolationException;
import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.config.MessageSourceConfig;
import no.nav.foreldrepenger.selvbetjening.error.ApiExceptionHandler;
import no.nav.foreldrepenger.selvbetjening.http.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.vedlegg.AttachmentTooLargeException;
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;
import no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekker;

@Import({MellomlagringController.class, ApiExceptionHandler.class})
@WebMvcTest(controllers = MellomlagringController.class)
@ContextConfiguration(classes = {
    JacksonConfiguration.class, Image2PDFConverter.class, MessageSourceConfig.class})
class MellomlagringControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private KryptertMellomlagring kryptertMellomlagring;

    @MockBean
    @Qualifier(DELEGERENDE)
    private VedleggSjekker vedleggSjekker;

    @MockBean
    private Image2PDFConverter converter;

    @MockBean
    private TokenUtil tokenUtil;

    @Test
    void opplastingAvVedleggReturnererUUID() throws Exception {
        when(converter.convert(any())).thenAnswer(i -> i.getArguments()[0]);
        var pdf = fraResource("pdf/jks.jpg");
        var vedlegg = new MockMultipartFile("vedlegg", pdf);
        var result = mvc.perform(multipart(MellomlagringController.REST_STORAGE + "/" + YtelseMellomlagringType.FORELDREPENGER + "/vedlegg").file(
            vedlegg)).andExpect(status().isCreated()).andReturn();
        assertThat(result.getResolvedException()).isNull();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
    }

    @Test
    void hentVedleggOkMedUUID() throws Exception {
        var key = UUID.randomUUID();
        var result = mvc.perform(get(MellomlagringController.REST_STORAGE + "/" + YtelseMellomlagringType.FORELDREPENGER + "/vedlegg/{key}",
            key).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andReturn();
        assertThat(result.getResolvedException()).isNull();
    }

    @Test
    void hentVedleggHiverExceptionMedUlovligKey() throws Exception {
        var key = "<Special=key∁\uD835\uDD4A>";
        var result = mvc.perform(get(MellomlagringController.REST_STORAGE + "/" + YtelseMellomlagringType.FORELDREPENGER + "/vedlegg/{key}",
            key).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

        assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void skalMappeNettverksrelatertExceptionTilGeneriskException() throws Exception {
        var wrappedConnectivityException = new MultipartException("simulert wrapped connectivity exception", new EOFException());
        doThrow(wrappedConnectivityException).when(converter).convert(any());
        var file = new MockMultipartFile("vedlegg", "filename.pdf", MULTIPART_FORM_DATA_VALUE, "file content".getBytes());

        mvc.perform(multipart(MellomlagringController.REST_STORAGE + "/" + YtelseMellomlagringType.FORELDREPENGER + "/vedlegg").file(file))
            .andExpect(status().isInternalServerError())
            .andReturn();
    }

    @Test
    void skalMappeAttachmentsTooLargeExceptionTil413() throws Exception {
        doThrow(new AttachmentTooLargeException(DataSize.ofMegabytes(2), DataSize.ofMegabytes(1))).when(converter).convert(any());
        var file = new MockMultipartFile("vedlegg", "filename.pdf", MULTIPART_FORM_DATA_VALUE, "file content".getBytes());
        mvc.perform(multipart(MellomlagringController.REST_STORAGE + "/" + YtelseMellomlagringType.FORELDREPENGER + "/vedlegg").file(file))
            .andExpect(status().isPayloadTooLarge())
            .andReturn();
    }

}
