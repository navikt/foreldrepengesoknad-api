package no.nav.foreldrepenger.selvbetjening.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.catalina.connector.ClientAbortException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.input.EOFException;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartException;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.util.TokenUtil;


@ExtendWith(SpringExtension.class)
class ApiExceptionHandlerTest {


    @MockBean
    private TokenUtil tokenUtil;
    private ApiExceptionHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new ApiExceptionHandler(tokenUtil, null);
    }

    @Test
    void andreMultipartExceptionSkalHaVanligExceptionLøype() {
        when(tokenUtil.autentisertBrukerOrElseThrowException()).thenReturn(new Fødselsnummer("0000000"));
        var webrequest = mock(ServletWebRequest.class);
        when(webrequest.getRequest()).thenReturn(new MockHttpServletRequest("POST", "/path"));
        var exception = new MultipartException("MULTIPART", new EOFException());

        var objectResponseEntity = errorHandler.handleMultipartException(exception, webrequest);

        assertThat(objectResponseEntity).isNotNull();
    }

    @Test
    void vanligClientAbortExecptionSkalReturnereNullRespons() {
        var webrequest = mock(ServletWebRequest.class);
        when(webrequest.getRequest()).thenReturn(new MockHttpServletRequest("POST", "/path"));
        var exception = new ClientAbortException("ABORT", new EOFException());

        var objectResponseEntity = errorHandler.catchAll(exception, webrequest);

        assertThat(objectResponseEntity).isNull();
    }
}
