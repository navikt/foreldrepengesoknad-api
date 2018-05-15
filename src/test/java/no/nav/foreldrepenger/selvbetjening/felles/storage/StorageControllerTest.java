package no.nav.foreldrepenger.selvbetjening.felles.storage;


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
public class StorageControllerTest implements ApplicationContextAware {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate http;

    private String endpoint;
    private HttpEntity<String> entity;

    private static  ApplicationContext applicationContext;
    private static final String FNR = "12345678901";
    private static final String PAYLOAD = "en skikkelig, skikkelig, skikkelig (s3) nais søknad";

    @Before
    public void setup() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Authorization", "bearer " + JwtTokenGenerator.createSignedJWT(FNR).serialize());
        entity = new HttpEntity<>(PAYLOAD, headers);
        endpoint = "http://localhost:" + port + "/foreldrepengesoknad-api/rest/storage";
    }

    @Test
    public void first_store_payload_over_HTTP() {
        ResponseEntity<String> responseEntity = http.exchange(endpoint, HttpMethod.POST, entity, String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void then_retrieve_payload_over_HTTP() {
        ResponseEntity<String> getResponse = http.exchange(endpoint, HttpMethod.GET, entity, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isEqualTo(PAYLOAD);
    }

    @AfterClass
    public static void destroy(){
        applicationContext.getBean("stubbedLocalStackContainer", StubbedLocalStackContainer.class).stopContainer();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
