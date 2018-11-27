package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static java.time.LocalDateTime.now;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.AttachmentStorageHttpTest.getByteArrayResource;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import no.nav.foreldrepenger.selvbetjening.ApiApplicationLocal;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Barn;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Engangsstønad;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;
import no.nav.security.oidc.test.support.JwtTokenGenerator;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ApiApplicationLocal.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev, localstack")
@Tag("SlowTests")
public class InnsendingHttpTest {

    private static final String FNR = "12345678910";
    private static ApplicationContext applicationContext;
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate http;
    @Autowired
    private ObjectMapper mapper;
    private String endpoint;

    private AttachmentTestHttpHandler attachmentHttpHandler;

    @BeforeEach
    public void setup() {
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        endpoint = "http://localhost:" + port + "/rest/engangsstonad";
        attachmentHttpHandler = new AttachmentTestHttpHandler(http, port, FNR);
    }

    @Test
    @DisplayName("Add an attachment, and then post the Søknad with a reference to the attachment")
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
                new HttpEntity<>(payload, createHeaders(MediaType.APPLICATION_JSON)), String.class);
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

    private HttpHeaders createHeaders(MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", mediaType.toString());
        headers.add("Authorization", "bearer " + JwtTokenGenerator.createSignedJWT(FNR).serialize());
        return headers;
    }

    private String engangsstonad(URI location) {
        try {
            Engangsstønad engangsstønad = new Engangsstønad();
            engangsstønad.opprettet = now();

            Barn barn = new Barn();
            barn.erBarnetFødt = false;
            engangsstønad.barn = barn;

            Vedlegg vedlegg = new Vedlegg();
            vedlegg.url = location;
            engangsstønad.vedlegg.add(vedlegg);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(engangsstønad);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
