package no.nav.foreldrepenger.selvbetjening.oppslag;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import no.nav.foreldrepenger.selvbetjening.config.TokenUtilConfiguration;
import no.nav.foreldrepenger.selvbetjening.innsyn.InnsynConfig;
import no.nav.foreldrepenger.selvbetjening.innsyn.InnsynConnection;
import no.nav.security.token.support.spring.SpringTokenValidationContextHolder;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {"oppslag.uri: http://www.oppslag.no"})
@ContextConfiguration(classes = {
    TokenUtilConfiguration.class,
    SpringTokenValidationContextHolder.class })
@RestClientTest
class OppslagTest {

    private final OppslagConfig oppslagConfig = new OppslagConfig(URI.create("http://www.vg.no"), true);
    private final InnsynConfig innsynConfig = new InnsynConfig(URI.create("http://www.vg.no"), true);

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private RestTemplateBuilder oppslagBuilder;

    private OppslagTjeneste oppslag;

    @BeforeEach
    public void restOperations() {
        var oppslagConnection = new OppslagConnection(oppslagBuilder.build(), oppslagConfig);
        var innsynConnection = new InnsynConnection(null, innsynConfig);
        oppslag = new OppslagTjeneste(oppslagConnection, innsynConnection);
    }

    @Test
    void ping() {
        server
                .expect(ExpectedCount.once(), requestTo(oppslagConfig.pingURI()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK));
        oppslag.ping();
        server.verify();
    }
}
