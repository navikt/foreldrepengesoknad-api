package no.nav.foreldrepenger.selvbetjening.rest;

import no.nav.foreldrepenger.selvbetjening.rest.json.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class OppslagControllerTest {

    @InjectMocks
    private OppslagController oppslagController;

    @Test
    public void stubPersonInfo() {
        Person person = oppslagController.personinfo("fnr", true);
        assertThat(person).isNotNull();
    }

}
