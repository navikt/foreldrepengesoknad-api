package no.nav.foreldrepenger.selvbetjening.error;

import static no.nav.foreldrepenger.selvbetjening.config.MessageSourceConfig.BOKMÅL_LOCALE;
import static no.nav.foreldrepenger.selvbetjening.error.LocalizedExceptionHandlerTest.TestController;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;
import java.util.function.Supplier;

import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.selvbetjening.config.MessageSourceConfig;
import no.nav.foreldrepenger.selvbetjening.vedlegg.AttachmentTypeUnsupportedException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.common.util.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.vedlegg.AttachmentPasswordProtectedException;

import org.springframework.web.context.WebApplicationContext;

@Import({ApiExceptionHandler.class})
@WebMvcTest(controllers = TestController.class)
@ContextConfiguration(classes = {
    JacksonConfiguration.class,
    LocalizedExceptionHandlerTest.TestConfig.class,
    TestController.class,
    MessageSourceConfig.class})
@ExtendWith(SpringExtension.class)
class LocalizedExceptionHandlerTest {

    @MockBean
    private TokenUtil tokenUtil;

    //@Autowired
    private MockMvc mockMvc;

    @Autowired
    private MutableSupplier<Exception> exceptionSupplier;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void skalMappeApiFeilmeldingMedVisningsvennligFelt() throws Exception {
        exceptionSupplier.setDelegate(AttachmentPasswordProtectedException::new);
        test(get("/exception"),
            "Vedlegget kan ikke behandles ettersom det er passordbeskyttet. Vennligst prøv igjen med et annet vedlegg.");

        exceptionSupplier.setDelegate(() -> new AttachmentTypeUnsupportedException(MediaType.IMAGE_GIF));
        test(get("/exception"), "Vedlegg av typen image/gif er ikke støttet.");
    }

    @Test
    void skalBrukeRequestLocale() throws Exception {
        exceptionSupplier.setDelegate(AttachmentPasswordProtectedException::new);
        var engelskRequest = get("/exception").locale(Locale.ENGLISH);
        test(engelskRequest, "The attachment cannot be processed as it is password protected. Please try again with a different attachment.");

        var norskRequest = get("/exception").locale(BOKMÅL_LOCALE);
        test(norskRequest, "Vedlegget kan ikke behandles ettersom det er passordbeskyttet. Vennligst prøv igjen med et annet vedlegg.");
    }

    @Test
    void skalBrukeNorskSomFallbackLocale() throws Exception {
        exceptionSupplier.setDelegate(AttachmentPasswordProtectedException::new);
        var engelskRequest = get("/exception").locale(Locale.FRANCE);
        test(engelskRequest, "Vedlegget kan ikke behandles ettersom det er passordbeskyttet. Vennligst prøv igjen med et annet vedlegg.");
    }

    private void test(MockHttpServletRequestBuilder requestBuilder, String expectedValue) throws Exception {
        mockMvc.perform(requestBuilder).andExpect(status().isUnprocessableEntity()).andExpect(jsonPath("$.userfriendlyMessage").value(expectedValue));
    }

    @Configuration
    public static class TestConfig {
        @Bean
        public MutableSupplier<Exception> exceptionSupplier() {
            return new MutableSupplier<>(() -> new RuntimeException("Default exception"));
        }
    }

    @RestController
    @RequestMapping("/")
    @ConditionalOnNotProd
    public static class TestController {

        @Autowired
        private Supplier<Exception> exceptionSupplier;

        @GetMapping("/exception")
        public ResponseEntity<String> triggerException() throws Exception {
            throw exceptionSupplier.get();
        }
    }

    public static class MutableSupplier<T> implements Supplier<T> {
        private Supplier<T> delegate;

        public MutableSupplier(Supplier<T> initial) {
            this.delegate = initial;
        }

        public void setDelegate(Supplier<T> delegate) {
            this.delegate = delegate;
        }

        @Override
        public T get() {
            return delegate.get();
        }
    }
}
