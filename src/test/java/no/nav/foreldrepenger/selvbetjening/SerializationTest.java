package no.nav.foreldrepenger.selvbetjening;

import static java.time.LocalDateTime.now;
import static no.nav.foreldrepenger.selvbetjening.oppslag.OppslagTjenesteStub.personDto;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Barn;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Engangsstønad;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
public class SerializationTest {

    @Inject
    ObjectMapper mapper;

    @Test
    public void person_serialiaztion() throws IOException {
        test(personDto());
    }

    @Test
    public void engangstonad_deserialisation() throws IOException {
        Engangsstønad engangsstønad = new Engangsstønad();
        engangsstønad.setOpprettet(now());
        Barn barn = new Barn();
        barn.erBarnetFødt = false;
        engangsstønad.setBarn(barn);

        test(engangsstønad);
    }

    private void test(Object object) throws IOException {
        assertThat(object)
                .isEqualToComparingFieldByFieldRecursively(mapper.readValue(write(object), object.getClass()));
    }

    private String write(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }
}
