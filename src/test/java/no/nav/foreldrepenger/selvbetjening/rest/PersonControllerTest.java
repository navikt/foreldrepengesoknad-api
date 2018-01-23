package no.nav.foreldrepenger.selvbetjening.rest;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import no.nav.foreldrepenger.selvbetjening.rest.json.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static io.micrometer.prometheus.PrometheusConfig.DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PersonControllerTest {

    @Spy
    private MeterRegistry registry = new PrometheusMeterRegistry(DEFAULT);

    @InjectMocks
    private PersonController personController;

    @Test
    public void stubPersonInfo() {
        Person person = personController.personinfo("fnr", true);
        assertThat(person).isNotNull();
    }

}
