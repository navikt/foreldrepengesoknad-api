package no.nav.foreldrepenger.selvbetjening.felles.storage;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import no.nav.foreldrepenger.selvbetjening.ApplicationLocal;
import no.nav.foreldrepenger.selvbetjening.SlowTests;
import no.nav.foreldrepenger.selvbetjening.stub.StubbedLocalStackContainer;
import no.nav.security.spring.oidc.test.JwtTokenGenerator;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationLocal.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev, localstack")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Category(SlowTests.class)
public class SoknadStorageHttpTest implements ApplicationContextAware {

    private static final String FNR = "12345678901";
    private static ApplicationContext applicationContext;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate http;
    @Autowired
    private ObjectMapper mapper;
    private String endpoint;


    @AfterClass
    public static void destroy() {
        applicationContext.getBean("stubbedLocalStackContainer", StubbedLocalStackContainer.class).stopContainer();
    }

    @Before
    public void setup() {
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        endpoint = "http://localhost:" + port + "/foreldrepengesoknad-api/rest/storage";
    }

    private HttpHeaders createHeaders(MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", mediaType.toString());
        headers.add("Authorization", "bearer " + JwtTokenGenerator.createSignedJWT(FNR).serialize());
        return headers;
    }

    @Test
    public void store_and_retrieve_json_over_HTTP() {
        String payload = "en skikkelig, skikkelig, skikkelig (s3) nais søknad";

        ResponseEntity<String> responseEntity = http.exchange(endpoint, HttpMethod.POST, new HttpEntity<>(payload, createHeaders(MediaType.APPLICATION_JSON)), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getResponse = http.exchange(endpoint, HttpMethod.GET, new HttpEntity<>(createHeaders(MediaType.APPLICATION_JSON)), String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isEqualTo(payload);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
