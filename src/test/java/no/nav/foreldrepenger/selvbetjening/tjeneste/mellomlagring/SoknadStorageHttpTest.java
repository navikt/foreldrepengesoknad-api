package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.StorageController.REST_STORAGE;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.LOCAL;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.LOCALSTACK;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.TEST;
import static no.nav.security.oidc.test.support.JwtTokenGenerator.createSignedJWT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.ApiApplicationLocal;
import no.nav.foreldrepenger.selvbetjening.SlowTests;
import no.nav.foreldrepenger.selvbetjening.stub.StubbedLocalStackContainer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplicationLocal.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ LOCAL, LOCALSTACK, TEST })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Category(SlowTests.class)
public class SoknadStorageHttpTest extends AbstractTestExecutionListener {

    private static final String FNR = "12345678901";
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate http;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private StubbedLocalStackContainer stubbedLocalStackContainer;
    private URI endpoint;

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        stubbedLocalStackContainer.stopContainer();
    }

    @Before
    public void setup() {
        mapper.disable(FAIL_ON_EMPTY_BEANS);
        endpoint = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path(REST_STORAGE)
                .build()
                .toUri();
    }

    private HttpHeaders createHeaders(String mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, mediaType);
        headers.add(AUTHORIZATION, "bearer " + createSignedJWT(FNR).serialize());
        return headers;
    }

    @Test
    public void store_and_retrieve_json_over_HTTP() {
        String payload = "en skikkelig, skikkelig, skikkelig (s3) nais søknad";
        ResponseEntity<String> responseEntity = http.postForEntity(endpoint,
                new HttpEntity<>(payload, createHeaders(APPLICATION_JSON_VALUE)), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(NO_CONTENT);
        ResponseEntity<String> getResponse = http.exchange(endpoint, GET,
                new HttpEntity<>(createHeaders(APPLICATION_JSON_VALUE)), String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(OK);
        assertThat(getResponse.getBody()).isEqualTo(payload);
    }
}
