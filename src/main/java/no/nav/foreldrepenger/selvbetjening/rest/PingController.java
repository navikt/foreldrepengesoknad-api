package no.nav.foreldrepenger.selvbetjening.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

    public static final String PING = "/rest/ping";

    private final RestTemplate template;
    private final String uri;

    public PingController(RestTemplate template, @Value("${FPSOKNAD_MOTTAK_API_URL}") String uri) {
        this.template = template;
        this.uri = uri;
    }

    @GetMapping
    public ResponseEntity<String> ping(@RequestParam("navn") String navn) {
        String svar = template.getForObject(uri + "/mottak/dokmot/ping?navn=" + navn, String.class);
        return ResponseEntity.status(HttpStatus.OK).body(svar);
    }
}
