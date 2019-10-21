package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.lenient;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.pdf.PDFGenerator;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.MellomlagringTjeneste;
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;
import no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekker;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@EnableRetry
@ActiveProfiles("test")
public class RetryTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private InnsendingConfig cfg;
    @Mock
    private VedleggSjekker sjekker;
    private static final Image2PDFConverter converter = new Image2PDFConverter();
    @Mock
    private PDFGenerator gen;
    @Mock
    private MellomlagringTjeneste ml;

    @Test
    public void testRetry() throws Exception {
        URI uri = new URI("http://www.vg.no");
        lenient().when(restTemplate.getForObject(isNull(), eq(String.class)))
                .thenThrow(HttpServerErrorException.class).thenReturn("OK");
        lenient().when(cfg.getUri()).thenReturn(uri);
        InnsendingConnection conn = new InnsendingConnection(restTemplate, cfg, converter);
        InnsendingTjeneste tjeneste = new InnsendingTjeneste(conn, ml, sjekker, gen);
        System.out.println("XXXX " + tjeneste.ping());
    }

}
