package no.nav.foreldrepenger.selvbetjening.rest;

import static org.springframework.http.HttpStatus.OK;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@CrossOrigin
@RestController
@RequestMapping(PingController.PING)
public class PingController {

    public static final String PING = "/rest/ping";

    private final RestTemplate template;
    private final URI mottakUri;

    public PingController(RestTemplate template, @Value("${FPSOKNAD_MOTTAK_API_URL}") URI mottakUri) {
        this.template = template;
        this.mottakUri = mottakUri;
    }

    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> pingMottak(@RequestParam("navn") String navn) {
        return ResponseEntity.status(OK)
                .body(template.getForObject(UriComponentsBuilder
                        .fromUri(mottakUri)
                        .queryParam("navn", navn).build().toUri(), String.class));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [template=" + template + ", mottakUri=" + mottakUri + "]";
    }
}
