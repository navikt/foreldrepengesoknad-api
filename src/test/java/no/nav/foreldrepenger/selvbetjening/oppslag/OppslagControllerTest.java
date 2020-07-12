package no.nav.foreldrepenger.selvbetjening.oppslag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import no.nav.foreldrepenger.selvbetjening.oppslag.OppslagController;
import no.nav.foreldrepenger.selvbetjening.oppslag.OppslagTjeneste;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Person;
import no.nav.foreldrepenger.selvbetjening.oppslag.dto.PersonDto;

@ExtendWith(MockitoExtension.class)
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
