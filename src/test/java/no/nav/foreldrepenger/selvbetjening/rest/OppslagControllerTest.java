package no.nav.foreldrepenger.selvbetjening.rest;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import no.nav.foreldrepenger.selvbetjening.config.ApiConfiguration;
import no.nav.foreldrepenger.selvbetjening.rest.json.Person;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { ApiConfiguration.class, OppslagController.class, ApiTestConfig.class })
@TestPropertySource(properties = {
        "FPSOKNAD_MOTTAK_API_URL=http://mottak.com",
        "FPSOKNAD_OPPSLAG_API_URL=http://oppslag.com" })
public class OppslagControllerTest {

    @Inject
    private OppslagController oppslagController;

    @Test
    public void stubPersonInfo() {
        Person person = oppslagController.personinfo("fnr", true);
        assertThat(person).isNotNull();
    }

}
