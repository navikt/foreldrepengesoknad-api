package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Person;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.dto.PersonDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OppslagControllerTest {

    @Mock
    private OppslagTjeneste oppslagstjeneste;

    @InjectMocks
    private OppslagController oppslagController;

    @Test
    public void stubPersonInfo() {
        when(oppslagstjeneste.hentPerson()).thenReturn(new Person(new PersonDto()));
        Person person = oppslagController.personinfo();
        assertThat(person).isNotNull();
    }
}
