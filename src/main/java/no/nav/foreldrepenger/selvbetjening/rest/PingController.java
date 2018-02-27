package no.nav.foreldrepenger.selvbetjening.rest;

import static org.springframework.http.HttpStatus.OK;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.selvbetjening.service.MottakPingService;

@CrossOrigin
@RestController
@RequestMapping(PingController.PING)
public class PingController {

    public static final String PING = "/rest/ping";

    private final MottakPingService pingService;

    public PingController(MottakPingService pingService) {
        this.pingService = pingService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> pingMottak(@RequestParam("navn") String navn) {
        return ResponseEntity.status(OK).body(pingService.ping(navn));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingService=" + pingService + "]";
    }
}
