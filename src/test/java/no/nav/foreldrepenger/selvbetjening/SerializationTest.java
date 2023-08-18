package no.nav.foreldrepenger.selvbetjening;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.BarnFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.EngangsstønadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.MutableVedleggReferanse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JacksonConfiguration.class)
class SerializationTest {

    @Autowired
    ObjectMapper mapper;

    @Test
    void engangstonad_deserialisation() throws IOException {
        var barn = new BarnFrontend(null, 2,
            List.of(new MutableVedleggReferanse(LocalDate.now().minusWeeks(6).toString())),
            LocalDate.now().minusWeeks(1), null,
            null, null, null, false, false,
            null, null, null);


        var engangsstønad = new EngangsstønadFrontend(
            now(),
            "engangsstønad",
            null, null,
            barn,
            null, null, null, null, null, null);
        test(engangsstønad);
    }

    private void test(Object object) throws IOException {
        assertEquals(object, mapper.readValue(write(object), object.getClass()));
    }

    private String write(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }
}
