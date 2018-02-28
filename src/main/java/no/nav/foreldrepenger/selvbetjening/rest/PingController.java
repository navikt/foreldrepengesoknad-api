package no.nav.foreldrepenger.selvbetjening.rest;

import static java.util.stream.Collectors.toMap;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.OK;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.selvbetjening.service.ping.Pinger;
import no.nav.foreldrepenger.selvbetjening.util.Pair;

@CrossOrigin
@RestController
@RequestMapping(PingController.PING)
public class PingController {

    private static final Logger LOG = getLogger(PingController.class);

    public static final String PING = "/rest/ping";

    private final List<Pinger> pingServices;

    public PingController(Pinger... pingServices) {
        this.pingServices = Arrays.asList(pingServices);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<URI, Boolean>> ping(@RequestParam("navn") String navn) {
        LOG.info("Pinging backend services {}", pingServices);
        return ResponseEntity.status(OK)
                .body(pingServices.stream()
                        .map(s -> ping(s, navn))
                        .collect(toMap(pair -> pair.getFirst(), pair -> pair.getSecond())));

    }

    private static Pair<URI, Boolean> ping(Pinger pinger, String message) {
        try {
            pinger.ping(message);
            return Pair.of(pinger.baseUri(), true);
        } catch (Exception e) {
            return Pair.of(pinger.baseUri(), false);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingServices=" + pingServices + "]";
    }
}
