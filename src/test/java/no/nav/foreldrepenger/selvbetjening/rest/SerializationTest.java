package no.nav.foreldrepenger.selvbetjening.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.foreldrepenger.selvbetjening.FastTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.io.IOException;

import static no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.OppslagstjenesteStub.person;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@Category(FastTests.class)
@AutoConfigureJsonTesters
public class SerializationTest {

    @Inject
    ObjectMapper mapper;

    @Test
    public void testPersonSerialiaztion() throws IOException {
        test(person());
    }

    private void test(Object object) throws IOException {
        String serialized = write(object);
        Object deserialized = mapper.readValue(serialized, object.getClass());
        assertThat(object).isEqualTo(deserialized);
    }

    private String write(Object obj) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }
}
