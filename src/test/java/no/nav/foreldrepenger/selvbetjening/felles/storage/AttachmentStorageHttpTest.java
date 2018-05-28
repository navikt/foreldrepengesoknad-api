package no.nav.foreldrepenger.selvbetjening.felles.storage;


import no.nav.foreldrepenger.selvbetjening.ApplicationLocal;
import no.nav.foreldrepenger.selvbetjening.SlowTests;
import no.nav.foreldrepenger.selvbetjening.stub.StubbedLocalStackContainer;
import no.nav.security.spring.oidc.test.JwtTokenGenerator;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationLocal.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev,localstack")
@Category(SlowTests.class)
public class AttachmentStorageHttpTest implements ApplicationContextAware {

    private static final String FNR = "12345678901";
    private static final byte[] PDFSIGNATURE = {0x25, 0x50, 0x44, 0x46};
    private static ApplicationContext applicationContext;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate http;

    private String endpoint;

    @AfterClass
    public static void destroy() {
        applicationContext.getBean("stubbedLocalStackContainer", StubbedLocalStackContainer.class).stopContainer();
    }

    @Before
    public void setup() {
        endpoint = "http://localhost:" + port + "/foreldrepengesoknad-api/rest/storage/vedlegg";
    }

    @Test
    public void store_and_retrieve_pdf_over_http() {
        ByteArrayResource byteArrayResource = getByteArrayResource("pdf", "test.pdf");

        ResponseEntity<String> postResponse = postAttachmentAsMultipart("vedlegg", MediaType.APPLICATION_PDF, byteArrayResource);
        URI location = postResponse.getHeaders().getLocation();
        assertThat(location).isNotNull();
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<byte[]> getResponse = getAttachment(location);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isEqualTo(byteArrayResource.getByteArray());
        assertThat(getResponse.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PDF);
        assertThat(Arrays.copyOfRange(getResponse.getBody(), 0, PDFSIGNATURE.length)).isEqualTo(PDFSIGNATURE);
    }

    @Test
    public void store_and_retrieve_image_over_http() {
        ByteArrayResource byteArrayResource = getByteArrayResource("pdf", "nav-logo.png");

        ResponseEntity<String> postResponse = postAttachmentAsMultipart("vedlegg", MediaType.IMAGE_PNG, byteArrayResource);
        URI location = postResponse.getHeaders().getLocation();
        assertThat(location).isNotNull();
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<byte[]> getResponse = getAttachment(location);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isEqualTo(byteArrayResource.getByteArray());
        assertThat(getResponse.getHeaders().getContentType()).isEqualTo(MediaType.IMAGE_PNG);
    }

    @Test
    public void delete_pdf_over_http() {
        URI location = postAttachmentAsMultipart("vedlegg", MediaType.APPLICATION_PDF, getByteArrayResource("pdf", "test.pdf")).getHeaders().getLocation();
        assertThat(getAttachment(location).getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> deleteResponse = deleteAttachment(location);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(getAttachment(location).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void unauthorized_calls_returns_401() {
        URI location = postAttachmentAsMultipart("vedlegg", MediaType.APPLICATION_PDF, getByteArrayResource("pdf", "test.pdf")).getHeaders().getLocation();
        ResponseEntity<byte[]> getResponse = http.exchange(location, HttpMethod.GET, new HttpEntity<>(null, null), byte[].class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        ResponseEntity<String> deleteResponse = http.exchange(location, HttpMethod.DELETE, new HttpEntity<>(null, null), String.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<byte[]> getAttachment(URI location) {
        return http.exchange(location, HttpMethod.GET, new HttpEntity<>(null, createHeaders(MediaType.ALL)), byte[].class);
    }

    private ResponseEntity<String> postAttachmentAsMultipart(String name, MediaType mediaType, ByteArrayResource byteArrayResource) {
        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
        multipartRequest.add(name, new HttpEntity<>(byteArrayResource, createHeaders(mediaType)));
        return http.exchange(endpoint, HttpMethod.POST, new HttpEntity<>(multipartRequest, createHeaders(MediaType.MULTIPART_FORM_DATA)), String.class);
    }

    private ResponseEntity<String> deleteAttachment(URI location) {
        return http.exchange(location, HttpMethod.DELETE, new HttpEntity<>(null, createHeaders(MediaType.ALL)), String.class);
    }

    private HttpHeaders createHeaders(MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", mediaType.toString());
        headers.add("Authorization", "bearer " + JwtTokenGenerator.createSignedJWT(FNR).serialize());
        return headers;
    }


    private ByteArrayResource getByteArrayResource(final String path, final String filename) {
        try {
            return new ByteArrayResource(Files.readAllBytes(new ClassPathResource(Paths.get(path, filename).toString()).getFile().toPath())) {
                @Override
                public String getFilename() {
                    return filename;
                }
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
