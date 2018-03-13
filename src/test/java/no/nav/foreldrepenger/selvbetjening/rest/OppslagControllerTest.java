package no.nav.foreldrepenger.selvbetjening.rest;

import no.nav.foreldrepenger.selvbetjening.consumer.Oppslagstjeneste;
import no.nav.foreldrepenger.selvbetjening.consumer.json.PersonDto;
import no.nav.foreldrepenger.selvbetjening.rest.json.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OppslagControllerTest {

    @Mock
    private Oppslagstjeneste oppslagstjeneste;

    @InjectMocks
    private OppslagController oppslagController;

    @Test
    public void stubPersonInfo() {
        when(oppslagstjeneste.hentPerson(anyString())).thenReturn(new PersonDto());
        Person person = oppslagController.personinfo("fnr");
        assertThat(person).isNotNull();
    }

}
