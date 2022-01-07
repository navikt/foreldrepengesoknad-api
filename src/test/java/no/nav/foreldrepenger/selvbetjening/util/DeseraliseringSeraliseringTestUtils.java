package no.nav.foreldrepenger.selvbetjening.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DeseraliseringSeraliseringTestUtils {

    private static final Logger LOG = LoggerFactory.getLogger(DeseraliseringSeraliseringTestUtils.class);

    public static void testDeseraliseringProdusererSammeObjekt(Object søknad, ObjectMapper mapper) {
        testDeseraliseringProdusererSammeObjekt(søknad, mapper, false);
    }

    public static void testDeseraliseringProdusererSammeObjekt(Object søknad, ObjectMapper mapper, boolean log) {
        try {
            if (log) {
                LOG.info("{}", søknad);
            }
            var serialized = serialize(søknad, mapper);
            if (log) {
                LOG.info("Serialized as {}", serialized);
            }
            var deserialized = mapper.readValue(serialized, søknad.getClass());
            if (log) {
                LOG.info("{}", deserialized);
            }
            assertEquals(søknad, deserialized);
        } catch (Exception e) {
            LOG.error("Oops", e);
            fail(søknad.getClass().getSimpleName() + " failed");
        }
    }

    public static String serialize(Object obj, ObjectMapper mapper) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }
}
