package no.nav.foreldrepenger.selvbetjening.felles.storage;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import no.nav.foreldrepenger.selvbetjening.ApplicationLocal;
import no.nav.foreldrepenger.selvbetjening.SlowTests;
import no.nav.foreldrepenger.selvbetjening.stub.StubbedLocalStackContainer;
import no.nav.security.spring.oidc.test.JwtTokenGenerator;
import org.jetbrains.annotations.NotNull;
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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Category(SlowTests.class)
public class AttachmentStorageHttpTest implements ApplicationContextAware {

    private static final String FNR = "12345678901";
    private static final byte[] PDFSIGNATURE = {0x25, 0x50, 0x44, 0x46};
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
        endpoint = "http://localhost:" + port + "/foreldrepengesoknad-api/rest/storage/vedlegg";
    }

    @Test
    public void store_and_retrieve_attachment_over_http() {
        String path = "pdf";
        final String filename = "test.pdf";
        ByteArrayResource byteArrayResource = getByteArrayResource(path, filename);

        ResponseEntity<String> storeAttachmentResponse = http.exchange(endpoint, HttpMethod.POST, createMultiPartHttpEntity(byteArrayResource), String.class);
        assertThat(storeAttachmentResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI location = storeAttachmentResponse.getHeaders().getLocation();
        assertThat(location).isNotNull();

        ResponseEntity<byte[]> getAttachmentResponse = http.exchange(location, HttpMethod.GET, new HttpEntity<>(null, createHeaders(MediaType.ALL)), byte[].class);
        assertThat(getAttachmentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getAttachmentResponse.getBody()).isEqualTo(byteArrayResource.getByteArray());
        assertThat(getAttachmentResponse.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PDF);
        assertThat(Arrays.copyOfRange(getAttachmentResponse.getBody(), 0, PDFSIGNATURE.length)).isEqualTo(PDFSIGNATURE);
    }

    @NotNull
    private HttpEntity<MultiValueMap<String, Object>> createMultiPartHttpEntity(ByteArrayResource byteArrayResource) {
        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
        multipartRequest.add("vedlegg", new HttpEntity<>(byteArrayResource, createHeaders(MediaType.APPLICATION_PDF)));
        return new HttpEntity<>(multipartRequest, createHeaders(MediaType.MULTIPART_FORM_DATA));
    }

    private HttpHeaders createHeaders(MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", mediaType.toString());
        headers.add("Authorization", "bearer " + JwtTokenGenerator.createSignedJWT(FNR).serialize());
        return headers;
    }

    @NotNull
    private ByteArrayResource getByteArrayResource(final String path, final String filename) {
        return new ByteArrayResource(getBytes(Paths.get(path, filename).toString())) {
            @Override
            public String getFilename() {
                return filename;
            }
        };
    }

    private byte[] getBytes(String path) {
        try {
            return Files.readAllBytes(new ClassPathResource(path).getFile().toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
