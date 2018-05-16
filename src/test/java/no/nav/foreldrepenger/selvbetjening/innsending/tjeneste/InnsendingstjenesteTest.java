package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste;

import no.nav.foreldrepenger.selvbetjening.felles.attachments.Image2PDFConverter;
import no.nav.foreldrepenger.selvbetjening.innsending.json.*;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.Oppslag;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.PersonDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.BadRequestException;
import java.net.URI;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@RunWith(MockitoJUnitRunner.class)
public class InnsendingstjenesteTest {

    Innsendingstjeneste innsending;

    @Mock
    RestTemplate template;

    @Mock
    Oppslag oppslag;

    @Mock
    Image2PDFConverter converter;

    @Before
    public void setUp() throws Exception {
        innsending = new Innsendingstjeneste(new URI("uri"), template, oppslag, converter);

        when(oppslag.hentPerson()).thenReturn(new PersonDto());
        when(template.postForEntity(any(URI.class), any(HttpEntity.class), eq(Kvittering.class))).thenReturn(kvittering());
    }

    @Test
    public void testInnsendingAvEnkelEngangsstønad() {
        ResponseEntity<Kvittering> response = innsending.sendInn(engangsstønad(), new MultipartFile[] {});
        assertThat(response.getStatusCode()).isEqualByComparingTo(OK);
    }
    
    @Test
    public void testInnsendingAvEnkelForeldrepengesøknad() {
        ResponseEntity<Kvittering> response = innsending.sendInn(foreldrepengesøknad(), new MultipartFile[] {});
        assertThat(response.getStatusCode()).isEqualByComparingTo(OK);
    }

    @Test
    public void testAtExceptionKastesVedUkjentTypeSøknad() {
        BadRequestException e = assertThrows(BadRequestException.class, () -> innsending.sendInn(new Søknad(), new MultipartFile[] {}));
        assertThat(e.getMessage()).isEqualToIgnoringCase("Unknown application type");
    }

    private ResponseEntity<Kvittering> kvittering() {
        return new ResponseEntity<>(new Kvittering("ref", now()), OK);
    }

    private Engangsstønad engangsstønad() {
        Engangsstønad engangsstønad = new Engangsstønad();
        engangsstønad.opprettet = now();

        Barn barn = new Barn();
        barn.erBarnetFødt = false;
        engangsstønad.barn = barn;

        return engangsstønad;
    }

    private Foreldrepengesøknad foreldrepengesøknad() {
        Foreldrepengesøknad foreldrepengesøknad = new Foreldrepengesøknad();
        foreldrepengesøknad.opprettet = now();

        Barn barn = new Barn();
        barn.erBarnetFødt = true;
        foreldrepengesøknad.barn = barn;

        foreldrepengesøknad.situasjon = "adopsjon";

        return foreldrepengesøknad;
    }

}
