package no.nav.foreldrepenger.selvbetjening.oppslag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.foreldrepenger.selvbetjening.FastTests;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.OppslagController;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.OppslagTjeneste;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Person;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.dto.PersonDto;

@Category(FastTests.class)
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
