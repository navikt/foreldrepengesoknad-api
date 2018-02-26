package no.nav.foreldrepenger.selvbetjening.rest;

import static java.time.LocalDateTime.now;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import no.nav.foreldrepenger.selvbetjening.consumer.json.EngangsstonadDto;
import no.nav.foreldrepenger.selvbetjening.rest.json.Engangsstonad;

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

        if (stub) {
            engangsstonad.id = "69";
            engangsstonad.opprettet = now();
            return created(location(engangsstonad.id)).body(engangsstonad);
        }

        LOG.info("Mottak URL: " + mottakServiceUrl);
        String url = mottakServiceUrl + "/mottak";
        EngangsstonadDto dto = new RestTemplate().postForObject(url, entity(engangsstonad), EngangsstonadDto.class);

        return created(location(engangsstonad.id)).body(new Engangsstonad(dto));
    }

    private HttpEntity<EngangsstonadDto> entity(Engangsstonad engangsstonad) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-nav-apiKey", apiGatewayKey);
        return new HttpEntity<>(new EngangsstonadDto(engangsstonad), headers);
    }

    private URI location(String id) {
        return fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    }

}
