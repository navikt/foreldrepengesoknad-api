package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import org.springframework.test.context.support.AbstractTestExecutionListener;

/*
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplicationLocal.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ LOCAL, TEST })
@Tag("IntegrationTest")

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
*/
public class SoknadStorageHttpTest extends AbstractTestExecutionListener {

    /*
     * private static final String FNR = "12345678901";
     * 
     * @LocalServerPort private int port;
     * 
     * @Autowired private TestRestTemplate http;
     * 
     * @Autowired private ObjectMapper mapper; private URI endpoint;
     * 
     * @Before public void setup() { mapper.disable(FAIL_ON_EMPTY_BEANS); endpoint =
     * UriComponentsBuilder .newInstance() .scheme("http") .host("localhost")
     * .port(port) .path(REST_STORAGE) .build() .toUri(); }
     * 
     * private HttpHeaders createHeaders(String mediaType) { HttpHeaders headers =
     * new HttpHeaders(); headers.add(CONTENT_TYPE, mediaType);
     * headers.add(AUTHORIZATION, "bearer " + createSignedJWT(FNR).serialize());
     * return headers; }
     * 
     * @Test public void store_and_retrieve_json_over_HTTP() { String payload =
     * "en skikkelig, skikkelig, skikkelig (s3) nais søknad"; ResponseEntity<String>
     * responseEntity = http.postForEntity(endpoint, new HttpEntity<>(payload,
     * createHeaders(APPLICATION_JSON_VALUE)), String.class);
     * assertThat(responseEntity.getStatusCode()).isEqualTo(NO_CONTENT);
     * ResponseEntity<String> getResponse = http.exchange(endpoint, GET, new
     * HttpEntity<>(createHeaders(APPLICATION_JSON_VALUE)), String.class);
     * assertThat(getResponse.getStatusCode()).isEqualTo(OK);
     * assertThat(getResponse.getBody()).isEqualTo(payload); }
     */
}
