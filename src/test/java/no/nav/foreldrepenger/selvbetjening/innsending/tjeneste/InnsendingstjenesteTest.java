package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

import java.net.URI;

import javax.ws.rs.BadRequestException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import no.nav.foreldrepenger.selvbetjening.attachments.Image2PDFConverter;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.Innsendingstjeneste;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Søknad;

@RunWith(MockitoJUnitRunner.class)
public class InnsendingstjenesteTest {

    Innsendingstjeneste innsending;

    @Mock
    RestTemplate template;

    @Mock
    Image2PDFConverter converter;

    @Before
    public void setUp() throws Exception {
        innsending = new Innsendingstjeneste(new URI("uri"), template, converter);
        when(template.postForEntity(any(URI.class), any(Object.class), eq(Kvittering.class))).thenReturn(kvittering());
    }

    @Test
    public void testAtExceptionKastesVedUkjentTypeSøknad() {
        BadRequestException e = assertThrows(BadRequestException.class, () -> innsending.sendInn(new Søknad()));
        assertThat(e.getMessage()).isEqualToIgnoringCase("Unknown application type");
    }

    private ResponseEntity<Kvittering> kvittering() {
        return new ResponseEntity<>(new Kvittering(now(), "1", "OK", "2", "3"), OK);
    }
}
