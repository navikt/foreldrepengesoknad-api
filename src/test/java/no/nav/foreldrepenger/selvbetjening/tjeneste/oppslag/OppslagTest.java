package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import javax.ws.rs.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;

import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;
import no.nav.security.token.support.spring.SpringTokenValidationContextHolder;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = { "oppslag.uri: http://www.oppslag.no",
        "spring.cloud.vault.enabled=false" })
@ContextConfiguration(classes = { NotFoundException.class, OppslagConfig.class, TokenUtil.class,
        SpringTokenValidationContextHolder.class })
@RestClientTest

@Ignore
public class OppslagTest {

    @Mock
    TokenUtil tokenHandler;
    @Autowired
    private OppslagConfig oppslagConfig;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private RestTemplateBuilder builder;

    private OppslagConnection oppslagConnection;

    private OppslagTjeneste oppslag;

    @BeforeEach
    public void restOperations() {
        oppslagConnection = new OppslagConnection(builder.build(), oppslagConfig);
        oppslag = new OppslagTjeneste(oppslagConnection);
    }

    @Test
    public void ping() {
        server
                .expect(ExpectedCount.once(), requestTo(oppslagConfig.getPingURI()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK));
        oppslag.ping();
        server.verify();
    }
}
