package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import static no.nav.foreldrepenger.common.util.ResourceHandleUtil.bytesFra;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.innsending.InnsendingConnection;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Ettersending;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { JacksonConfiguration.class, FrontendSøknadTilSøknaDtoSeralizationDeseraliseringTest.InnsendingConnectionConfiguration.class})
class EttersendingTilDtoSeralizationDeseralizationTest {
    private static final Logger LOG = LoggerFactory.getLogger(EttersendingTilDtoSeralizationDeseralizationTest.class);

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private InnsendingConnection connection;

    @Test
    void testEttersendingMapper() {
        var ettersending = new Ettersending("foreldrepenger", "952003131", null, null, null);
        testDeseraliseringProdusererSammeObjekt(ettersending);
    }

    @Test
    void ettersendelseSeraliseringDeseraliseringTest() throws IOException {
        var ettersendelse = mapper.readValue(bytesFra("json/ettersendelse_I000044.json"), no.nav.foreldrepenger.selvbetjening.innsending.domain.Ettersending.class);
        testDeseraliseringProdusererSammeObjekt(ettersendelse);
    }

        private void testDeseraliseringProdusererSammeObjekt(Ettersending ettersendingFraFrontend) {
        testDeseraliseringProdusererSammeObjekt(ettersendingFraFrontend, false);
    }

    private void testDeseraliseringProdusererSammeObjekt(Ettersending ettersendingFraFrontend, boolean log) {
        var ettersending = connection.body(ettersendingFraFrontend);
        try {
            if (log) {
                LOG.info("{}", ettersending);
            }
            var serialized = serialize(ettersending);
            if (log) {
                LOG.info("Serialized as {}", serialized);
            }
            var deserialized = mapper.readValue(serialized, ettersending.getClass());
            if (log) {
                LOG.info("{}", deserialized);
            }
            assertEquals(ettersending, deserialized);
        } catch (Exception e) {
            LOG.error("Oops", e);
            fail(ettersending.getClass().getSimpleName() + " failed");
        }
    }

    private String serialize(Object obj) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }
}
