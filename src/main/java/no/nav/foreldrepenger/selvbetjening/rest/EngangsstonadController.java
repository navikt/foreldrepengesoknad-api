package no.nav.foreldrepenger.selvbetjening.rest;

import no.nav.foreldrepenger.selvbetjening.consumer.json.EngangsstonadDto;
import no.nav.foreldrepenger.selvbetjening.rest.json.Engangsstonad;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import static java.time.LocalDateTime.now;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin
@RestController
@RequestMapping(EngangsstonadController.REST_ENGANGSSTONAD)
public class EngangsstonadController {

    public static final String REST_ENGANGSSTONAD = "/rest/engangsstonad";

    private static final Logger LOG = getLogger(EngangsstonadController.class);

    @Value("${FPSOKNAD_MOTTAK_API_URL}")
    private String mottakServiceUrl;

    @Value("${FORELDREPENGESOKNAD_API_FPSOKNAD_MOTTAK_API_APIKEY_PASSWORD}")
    private String apiGatewayKey;

    @GetMapping("/{id}")
    public Engangsstonad hentEngangsstonad(@PathVariable String id) {
        Engangsstonad engangsstonad = Engangsstonad.stub();
        engangsstonad.id = id;
        return engangsstonad;
    }

    @PostMapping
    public ResponseEntity<Engangsstonad> opprettEngangsstonad(@RequestBody Engangsstonad engangsstonad,
            @RequestParam(name = "stub", defaultValue = "false", required = false) Boolean stub) {
        LOG.info("Poster engangsstønad {}", stub ? "(stub)" : "");

        engangsstonad.opprettet = now();

        if (stub) {
            engangsstonad.id = "69";
            return ok(engangsstonad);
        }

        String url = mottakServiceUrl + "/mottak/dokmot/send";
        LOG.info("Mottak URL: " + url);

        new RestTemplate().postForEntity(url, entity(engangsstonad), String.class);
        return ok(engangsstonad);
    }

    private HttpEntity<EngangsstonadDto> entity(Engangsstonad engangsstonad) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-nav-apiKey", apiGatewayKey);
        return new HttpEntity<>(new EngangsstonadDto(engangsstonad), headers);
    }

}
