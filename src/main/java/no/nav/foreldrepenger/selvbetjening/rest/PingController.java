package no.nav.foreldrepenger.selvbetjening.rest;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@CrossOrigin
@RestController
@RequestMapping(PingController.PING)
public class PingController {

    private static final Logger LOG = getLogger(PingController.class);

    public static final String PING = "/rest/ping";

    private final RestTemplate template;
    private final String uri;

    public PingController(RestTemplate template, @Value("${FPSOKNAD_MOTTAK_API_URL}") String uri) {
        this.template = template;
        this.uri = uri;
    }

    @GetMapping
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> ping(@RequestParam("navn") String navn) {
        String url = uri + "/mottak/dokmot/ping?navn=" + navn;
        LOG.info("Pinging {}", uri);
        String svar = template.getForObject(url, String.class);
        return ResponseEntity.status(HttpStatus.OK).body(svar);
    }
}
