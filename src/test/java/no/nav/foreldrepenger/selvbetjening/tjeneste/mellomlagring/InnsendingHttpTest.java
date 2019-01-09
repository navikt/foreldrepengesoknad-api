package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import no.nav.foreldrepenger.selvbetjening.ApiApplicationLocal;
import no.nav.foreldrepenger.selvbetjening.SlowTests;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.*;
import no.nav.security.oidc.test.support.JwtTokenGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.AttachmentStorageHttpTest.getByteArrayResource;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = ApiApplicationLocal.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev, localstack")
@RunWith(SpringRunner.class)
@Category(SlowTests.class)
public class InnsendingHttpTest {

    private static final String FNR = "12345678910";
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate http;
    @Autowired
    private ObjectMapper mapper;
    private String endpoint;

    private AttachmentTestHttpHandler attachmentHttpHandler;

    @Before
    public void setup() {
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        endpoint = "http://localhost:" + port + "/rest/soknad";
        attachmentHttpHandler = new AttachmentTestHttpHandler(http, port, FNR);
    }

    @Test
    public void sendSoknad() {
        URI attchmentLocation = postAttachmentOverHttp();
        ResponseEntity<String> response = postSoknadOverHttp(attchmentLocation);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(attachmentHttpHandler.getAttachment(attchmentLocation).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<String> postSoknadOverHttp(URI location) {
        String payload = engangsstonad(location);
        return http.exchange(endpoint, HttpMethod.POST,
                new HttpEntity<>(payload, createHeaders()), String.class);
    }

    private URI postAttachmentOverHttp() {
        ByteArrayResource byteArrayResource = getByteArrayResource("pdf", "test.pdf");
        ResponseEntity<String> postResponse = attachmentHttpHandler.postMultipart("vedlegg", MediaType.APPLICATION_PDF,
                byteArrayResource);
        URI location = postResponse.getHeaders().getLocation();
        assertThat(location).isNotNull();
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return location;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", APPLICATION_JSON);
        headers.add("Authorization", "bearer " + JwtTokenGenerator.createSignedJWT(FNR).serialize());
        return headers;
    }

    private String engangsstonad(URI location) {
        try {
            Engangsstønad engangsstønad = new Engangsstønad();
            engangsstønad.opprettet = now();
            engangsstønad.type = "engangsstønad";

            Barn barn = new Barn();
            barn.erBarnetFødt = false;
            engangsstønad.barn = barn;


            AnnenForelder annenForelder = new AnnenForelder();
            annenForelder.kanIkkeOppgis = true;
            engangsstønad.annenForelder = annenForelder;

            Utenlandsopphold utenlandsopphold = new Utenlandsopphold();
            utenlandsopphold.iNorgeNeste12Mnd = true;
            utenlandsopphold.iNorgeSiste12Mnd = true;
            utenlandsopphold.iNorgePåHendelsestidspunktet = true;
            utenlandsopphold.senereOpphold = emptyList();
            utenlandsopphold.tidligereOpphold = emptyList();
            engangsstønad.informasjonOmUtenlandsopphold = utenlandsopphold;

            engangsstønad.erEndringssøknad = false;

            Vedlegg vedlegg = new Vedlegg();
            vedlegg.url = location;
            engangsstønad.vedlegg.add(vedlegg);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(engangsstønad);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
