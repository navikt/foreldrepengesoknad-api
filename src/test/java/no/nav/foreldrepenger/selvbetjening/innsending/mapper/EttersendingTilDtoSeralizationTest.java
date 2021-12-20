package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.innsending.InnsendingConnection;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Ettersending;
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { JacksonConfiguration.class, EttersendingTilDtoSeralizationTest.InnsendingConnectionConfiguration.class})
class EttersendingTilDtoSeralizationTest {
    private static final Logger LOG = LoggerFactory.getLogger(EttersendingTilDtoSeralizationTest.class);

    @TestConfiguration
    static class InnsendingConnectionConfiguration {
        @Bean
        InnsendingConnection mockInnsendingConnection() {
            return new InnsendingConnection(null, null, new Image2PDFConverter());
        }
    }

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private InnsendingConnection connection;

    @Test
    void testBody() {
        var ettersending = new Ettersending();
        ettersending.setType("foreldrepenger");
        ettersending.setSaksnummer("352003131");
        testDeseraliseringProdusererSammeObjekt(ettersending);
    }



    private void testDeseraliseringProdusererSammeObjekt(Ettersending ettersending) {
        try {
            var søknadOLDDto = connection.body(ettersending);
            var seralized_old = serialize(søknadOLDDto);
            var deseralized_old = mapper.readValue(seralized_old, no.nav.foreldrepenger.common.domain.felles.Ettersending.class);

            var søknadNY = connection.tilEttersendingNY(ettersending);
            var seralized_ny = serialize(søknadNY);
            var deseralized_ny = mapper.readValue(seralized_ny, no.nav.foreldrepenger.common.domain.felles.Ettersending.class);
            assertThat(deseralized_old).isEqualTo(deseralized_ny);
        } catch (Exception e) {
            LOG.error("Oops", e);
            fail(ettersending.getClass().getSimpleName() + " failed");
        }
    }

    private String serialize(Object obj) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }
}
