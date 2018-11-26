package no.nav.foreldrepenger.selvbetjening.felles.storage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import no.nav.foreldrepenger.selvbetjening.ApiApplicationLocal;
import no.nav.foreldrepenger.selvbetjening.SlowTests;
import no.nav.foreldrepenger.selvbetjening.stub.StubbedLocalStackContainer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplicationLocal.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev,localstack")
@Category(SlowTests.class)
public class AttachmentStorageHttpTest implements ApplicationContextAware {

    private static final String FNR = "12345678910";
    private static final byte[] PDFSIGNATURE = { 0x25, 0x50, 0x44, 0x46 };
    private static ApplicationContext applicationContext;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    public AttachmentTestHttpHandler http;

    @AfterClass
    public static void destroy() {
        applicationContext.getBean("stubbedLocalStackContainer", StubbedLocalStackContainer.class).stopContainer();
    }

    @Before
    public void setup() {
        http = new AttachmentTestHttpHandler(testRestTemplate, port, FNR);
    }

    @Test
    public void store_and_retrieve_pdf_over_http() {
        ByteArrayResource byteArrayResource = getByteArrayResource("pdf", "test.pdf");

        ResponseEntity<String> postResponse = http.postMultipart("vedlegg", MediaType.APPLICATION_PDF,
                byteArrayResource);
        URI location = postResponse.getHeaders().getLocation();
        assertThat(location).isNotNull();
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<byte[]> getResponse = http.getAttachment(location);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isEqualTo(byteArrayResource.getByteArray());
        assertThat(getResponse.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PDF);
        assertThat(Arrays.copyOfRange(getResponse.getBody(), 0, PDFSIGNATURE.length)).isEqualTo(PDFSIGNATURE);
    }

    @Test
    public void store_and_retrieve_image_over_http() {
        ByteArrayResource byteArrayResource = getByteArrayResource("pdf", "nav-logo.png");

        ResponseEntity<String> postResponse = http.postMultipart("vedlegg", MediaType.IMAGE_PNG, byteArrayResource);
        URI location = postResponse.getHeaders().getLocation();
        assertThat(location).isNotNull();
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<byte[]> getResponse = http.getAttachment(location);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isEqualTo(byteArrayResource.getByteArray());
        assertThat(getResponse.getHeaders().getContentType()).isEqualTo(MediaType.IMAGE_PNG);
    }

    @Test
    public void delete_pdf_over_http() {
        URI location = http.postMultipart("vedlegg", MediaType.APPLICATION_PDF, getByteArrayResource("pdf", "test.pdf"))
                .getHeaders().getLocation();
        assertThat(http.getAttachment(location).getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> deleteResponse = http.delete(location);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(http.getAttachment(location).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void unauthorized_calls_returns_401() {
        URI location = http.postMultipart("vedlegg", MediaType.APPLICATION_PDF, getByteArrayResource("pdf", "test.pdf"))
                .getHeaders().getLocation();
        ResponseEntity<byte[]> getResponse = testRestTemplate.exchange(location, HttpMethod.GET,
                new HttpEntity<>(null, null), byte[].class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        ResponseEntity<String> deleteResponse = testRestTemplate.exchange(location, HttpMethod.DELETE,
                new HttpEntity<>(null, null), String.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    public static ByteArrayResource getByteArrayResource(final String path, final String filename) {
        try {
            return new ByteArrayResource(Files
                    .readAllBytes(new ClassPathResource(Paths.get(path, filename).toString()).getFile().toPath())) {
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
