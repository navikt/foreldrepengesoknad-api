package no.nav.foreldrepenger.selvbetjening.error;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import no.nav.boot.conditionals.ConditionalOnNotProd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.common.domain.Søker;
import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.http.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.http.filters.RequestCachingFilter;
import no.nav.security.token.support.core.api.Unprotected;


@Import({ApiExceptionHandler.class})
@WebMvcTest(controllers = CachedRequestLoggingTest.TestController.class)
@ContextConfiguration(classes = {
    JacksonConfiguration.class,
    CachedRequestLoggingTest.TestController.class,
    RequestCachingFilter.class})
@ExtendWith(OutputCaptureExtension.class)
class CachedRequestLoggingTest {
    @MockitoBean
    private TokenUtil tokenUtil;
    @Autowired
    private ApiExceptionHandler errorHandler;
    @Autowired
    private MockMvc mockMvc;


    @Test
    void ugyldigJsonVedInnsendingTriggerBodyLogging(CapturedOutput output) throws Exception {
        var ugyldigJson = """
                {
                    "søknadsRolle": "helt ukjent verdi",
                    "målform": "denne også"
                }
            """;
        var forventetLogg = "secureLogger - [/rest/soknad] " + ugyldigJson;
        mockMvc.perform(post("/rest/soknad")
                .content(ugyldigJson)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity());
        var logs = output.getOut();
        assertThat(logs).contains(forventetLogg);
    }

    @RestController
    @RequestMapping("/rest")
    @Unprotected
    @ConditionalOnNotProd
    static class TestController {

        @PostMapping("/soknad")
        ResponseEntity<String> testEndpoint(@RequestBody Søker body) {
            return ResponseEntity.ok("Feiler før vi kommer hit");
        }
    }
}
