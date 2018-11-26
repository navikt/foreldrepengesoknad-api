package no.nav.foreldrepenger.selvbetjening;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Barn;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Engangsstønad;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.io.IOException;

import static java.time.LocalDateTime.now;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.OppslagTjenesteStub.person;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@Category(FastTests.class)
@AutoConfigureJsonTesters
public class SerializationTest {

    @Inject
    ObjectMapper mapper;

    @Test
    public void person_serialiaztion() throws IOException {
        test(person());
    }

    @Test
    public void engangstonad_deserialisation() throws IOException {
        Engangsstønad engangsstønad = new Engangsstønad();
        engangsstønad.opprettet = now();

        Barn barn = new Barn();
        barn.erBarnetFødt = false;
        engangsstønad.barn = barn;

        test(engangsstønad);
    }

    private void test(Object object) throws IOException {
        String serialized = write(object);
        Object deserialized = mapper.readValue(serialized, object.getClass());
        assertThat(object).isEqualToComparingFieldByFieldRecursively(deserialized);
    }

    private String write(Object obj) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }
}
