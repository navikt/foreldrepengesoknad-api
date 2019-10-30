package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import no.nav.foreldrepenger.selvbetjening.ApiApplicationLocal;

@SpringBootTest(classes = ApiApplicationLocal.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local,localstack,test")
@Tag("IntegrationTest")
public class AttachmentStorageHttpTest extends AbstractTestExecutionListener {

    /*
     * private static final String FNR = "12345678910";
     * 
     * @LocalServerPort private int port;
     * 
     * @Autowired private TestRestTemplate testRestTemplate; public
     * AttachmentTestHttpHandler http;
     * 
     * @BeforeEach public void setup() { http = new
     * AttachmentTestHttpHandler(testRestTemplate, port, FNR); }
     * 
     * @Test public void store_and_retrieve_image_over_http() { ByteArrayResource
     * byteArrayResource = getByteArrayResource("pdf", "nav-logo.png");
     * 
     * ResponseEntity<String> postResponse = http.postMultipart("vedlegg",
     * MediaType.IMAGE_PNG, byteArrayResource); URI location =
     * postResponse.getHeaders().getLocation(); assertThat(location).isNotNull();
     * assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
     * 
     * ResponseEntity<byte[]> getResponse = http.getAttachment(location);
     * assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
     * assertThat(getResponse.getBody()).isEqualTo(byteArrayResource.getByteArray())
     * ; assertThat(getResponse.getHeaders().getContentType()).isEqualTo(MediaType.
     * IMAGE_PNG); }
     * 
     * @Test public void delete_pdf_over_http() { URI location =
     * http.postMultipart("vedlegg", MediaType.APPLICATION_PDF,
     * getByteArrayResource("pdf", "test.pdf")) .getHeaders().getLocation();
     * assertThat(http.getAttachment(location).getStatusCode()).isEqualTo(HttpStatus
     * .OK);
     * 
     * ResponseEntity<String> deleteResponse = http.delete(location);
     * assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
     * 
     * assertThat(http.getAttachment(location).getStatusCode()).isEqualTo(HttpStatus
     * .NOT_FOUND); }
     * 
     * @Test public void unauthorized_calls_returns_401() { URI location =
     * http.postMultipart("vedlegg", MediaType.APPLICATION_PDF,
     * getByteArrayResource("pdf", "test.pdf")) .getHeaders().getLocation();
     * ResponseEntity<byte[]> getResponse = testRestTemplate.exchange(location,
     * HttpMethod.GET, new HttpEntity<>(null, null), byte[].class);
     * assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
     * 
     * ResponseEntity<String> deleteResponse = testRestTemplate.exchange(location,
     * HttpMethod.DELETE, new HttpEntity<>(null, null), String.class);
     * assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED)
     * ; }
     * 
     * public static ByteArrayResource getByteArrayResource(final String path, final
     * String filename) { try { return new ByteArrayResource(Files .readAllBytes(new
     * ClassPathResource(Paths.get(path, filename).toString()).getFile().toPath()))
     * {
     * 
     * @Override public String getFilename() { return filename; } }; } catch
     * (IOException e) { throw new RuntimeException(e); } }
     */
}
