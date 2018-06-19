package no.nav.foreldrepenger.selvbetjening.felles.storage;

import no.nav.security.spring.oidc.test.JwtTokenGenerator;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;

public class AttachmentTestHttpHandler {

    private final TestRestTemplate http;
    private final String endpoint;
    private final String fnr;

    public AttachmentTestHttpHandler(TestRestTemplate http, int port, String fnr){
        this.http = http;
        this.endpoint = "http://localhost:" + port + "/foreldrepengesoknad-api/rest/storage/vedlegg";
        this.fnr = fnr;
    }

    public ResponseEntity<byte[]> getAttachment(URI location) {
        return http.exchange(location, HttpMethod.GET, new HttpEntity<>(null, createHeaders(MediaType.ALL)), byte[].class);
    }

    public ResponseEntity<String> postMultipart(String name, MediaType mediaType, ByteArrayResource byteArrayResource) {
        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
        multipartRequest.add(name, new HttpEntity<>(byteArrayResource, createHeaders(mediaType)));
        return http.exchange(endpoint, HttpMethod.POST, new HttpEntity<>(multipartRequest, createHeaders(MediaType.MULTIPART_FORM_DATA)), String.class);
    }

    public ResponseEntity<String> delete(URI location) {
        return http.exchange(location, HttpMethod.DELETE, new HttpEntity<>(null, createHeaders(MediaType.ALL)), String.class);
    }

    public HttpHeaders createHeaders(MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", mediaType.toString());
        headers.add("Authorization", "bearer " + JwtTokenGenerator.createSignedJWT(fnr).serialize());
        return headers;
    }

}
