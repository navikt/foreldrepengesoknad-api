package no.nav.foreldrepenger.selvbetjening.oppslag;

import no.nav.foreldrepenger.selvbetjening.FastTests;
import no.nav.foreldrepenger.selvbetjening.oppslag.json.Person;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.Oppslagstjeneste;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.PersonDto;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Category(FastTests.class)
@RunWith(MockitoJUnitRunner.class)
public class OppslagControllerTest {

    @Mock
    private Oppslagstjeneste oppslagstjeneste;

    @InjectMocks
    private OppslagController oppslagController;

    @Test
    public void stubPersonInfo() {
        when(oppslagstjeneste.hentPerson()).thenReturn(new PersonDto());
        Person person = oppslagController.personinfo();
        assertThat(person).isNotNull();
    }
}
