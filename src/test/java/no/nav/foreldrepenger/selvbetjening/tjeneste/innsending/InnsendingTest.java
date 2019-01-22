package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Søknad;
import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;
import no.nav.security.spring.oidc.SpringOIDCRequestContextHolder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
@TestPropertySource(properties = { "FPSOKNAD_MOTTAK_API_URL = http://www.mottak.no/api" })
@ContextConfiguration(classes = { NotFoundException.class, InnsendingConfig.class, Image2PDFConverter.class,
        TokenUtil.class, SpringOIDCRequestContextHolder.class })
@RestClientTest

public class InnsendingTest {

    @Mock
    TokenUtil tokenHandler;
    @Autowired
    private InnsendingConfig innsendingConfig;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private RestTemplateBuilder builder;

    private InnsendingTjeneste innsending;
    @Autowired
    private Image2PDFConverter converter;

    @Before
    public void init() {
        if (innsending == null) {
            innsending = new InnsendingTjeneste(new InnsendingConnection(builder
                    .build(), innsendingConfig, converter));
        }
    }

    // @Test
    public void ping() {
        server.expect(ExpectedCount.once(), requestTo(innsendingConfig.getPingURI()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK));
        innsending.ping();
        server.verify();
    }

    @Test(expected = BadRequestException.class)
    public void unknownType() {
        server.expect(ExpectedCount.once(), requestTo(innsendingConfig.getInnsendingURI()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK));
        innsending.sendInn(new Søknad());

    }
}
