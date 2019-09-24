package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import java.net.URI;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import no.nav.security.token.support.test.JwtTokenGenerator;

public class AttachmentTestHttpHandler {

    private final TestRestTemplate http;
    private final String endpoint;
    private final String fnr;

    public AttachmentTestHttpHandler(TestRestTemplate http, int port, String fnr) {
        this.http = http;
        this.endpoint = "http://localhost:" + port + "/rest/storage/vedlegg";
        this.fnr = fnr;
    }

    public ResponseEntity<byte[]> getAttachment(URI location) {
        return http.exchange(location, HttpMethod.GET, new HttpEntity<>(null, createHeaders(MediaType.ALL)),
                byte[].class);
    }

    public ResponseEntity<String> postMultipart(String name, MediaType mediaType, ByteArrayResource byteArrayResource) {
        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
        multipartRequest.add(name, new HttpEntity<>(byteArrayResource, createHeaders(mediaType)));
        return http.exchange(endpoint, HttpMethod.POST,
                new HttpEntity<>(multipartRequest, createHeaders(MediaType.MULTIPART_FORM_DATA)), String.class);
    }

    public ResponseEntity<String> delete(URI location) {
        return http.exchange(location, HttpMethod.DELETE, new HttpEntity<>(null, createHeaders(MediaType.ALL)),
                String.class);
    }

    public HttpHeaders createHeaders(MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", mediaType.toString());
        headers.add("Authorization", "bearer " + JwtTokenGenerator.createSignedJWT(fnr).serialize());
        return headers;
    }

}
