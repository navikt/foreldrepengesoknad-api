package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import javax.ws.rs.NotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
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

import no.nav.foreldrepenger.selvbetjening.FastTests;
import no.nav.foreldrepenger.selvbetjening.util.TokenHelper;
import no.nav.security.spring.oidc.SpringOIDCRequestContextHolder;

@Category(FastTests.class)
@RunWith(SpringRunner.class)
@TestPropertySource(properties = { "FPSOKNAD_MOTTAK_API_URL = http://www.mottak.no/api",
        "FPSOKNAD_OPPSLAG_API_URL: http://www.oppslag.no/api" })
@ContextConfiguration(classes = { NotFoundException.class, InnsynConfig.class, TokenHelper.class,
        SpringOIDCRequestContextHolder.class })
@RestClientTest

public class InnsynTest {

    @Mock
    TokenHelper tokenHandler;
    @Autowired
    private InnsynConfig innsynConfig;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private RestTemplateBuilder builder;

    private InnsynTjeneste innsyn;

    @Before
    public void init() {
        innsyn = new InnsynTjeneste(new InnsynConnection(builder
                .build(), innsynConfig));
    }

    @Test
    public void uttaksplan() {
        server
                .expect(ExpectedCount.once(), requestTo(innsynConfig.getUttakURI("42")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK));
        innsyn.hentUttaksplan("42");
        server.verify();
    }
}
