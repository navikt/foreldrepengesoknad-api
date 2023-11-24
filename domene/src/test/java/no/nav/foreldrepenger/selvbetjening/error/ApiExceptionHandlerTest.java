package no.nav.foreldrepenger.selvbetjening.error;

import no.nav.foreldrepenger.common.util.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.innsending.InnsendingController;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@RunWith(SpringRunner.class)
public class ApiExceptionHandlerTest {

    @MockBean
    TokenUtil tokenUtil;


    @Test
    public void testJoda() throws Exception {
        var controllerAdvice = new ApiExceptionHandler(tokenUtil);
        try {
            controllerAdvice.handleHttpMessageNotReadable(null, null, null, null);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
