package no.nav.foreldrepenger.selvbetjening;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad.EngangsstønadDto;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JacksonConfiguration.class)
class SerializationTest {

    @Autowired
    ObjectMapper mapper;

    @Test
    void engangstonad_deserialisation() throws IOException {
        var barn = new BarnDto(null, 2,
            LocalDate.now().minusWeeks(1), null,
            null, null, false, false, null,
            List.of(new MutableVedleggReferanseDto(LocalDate.now().minusWeeks(6).toString())),
            null, null, null);


        var engangsstønad = new EngangsstønadDto(LocalDate.now(), barn, null, null, null);
        test(engangsstønad);
    }

    private void test(Object object) throws IOException {
        assertEquals(object, mapper.readValue(write(object), object.getClass()));
    }

    private String write(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }
}