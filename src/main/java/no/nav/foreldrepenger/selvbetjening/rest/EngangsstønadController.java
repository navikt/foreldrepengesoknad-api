package no.nav.foreldrepenger.selvbetjening.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.foreldrepenger.selvbetjening.consumer.json.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.rest.json.Engangsstønad;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static java.time.LocalDateTime.now;
import static no.nav.foreldrepenger.selvbetjening.rest.EngangsstønadController.REST_ENGANGSSTONAD;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin
@RestController
@RequestMapping(REST_ENGANGSSTONAD)
public class EngangsstønadController {

    public static final String REST_ENGANGSSTONAD = "/rest/engangsstonad";

    private static final Logger LOG = getLogger(EngangsstønadController.class);

    private final RestTemplate template;

    private final URI mottakServiceUrl;

    public EngangsstønadController(RestTemplate template, @Value("${FPSOKNAD_MOTTAK_API_URL}") URI baseUri) {
        this.template = template;
        this.mottakServiceUrl = mottakUriFra(baseUri);
    }

    @GetMapping("/{id}")
    public Engangsstønad hentEngangsstonad(@PathVariable String id) {
        return Engangsstønad.stub();
    }

    @PostMapping
    public ResponseEntity<Engangsstønad> opprettEngangsstonad(@RequestBody Engangsstønad engangsstønad,
                                                              @RequestParam(name = "stub", defaultValue = "false", required = false) Boolean stub) throws Exception {
        LOG.info("Poster engangsstønad {}", stub ? "(stub)" : "");

        engangsstønad.opprettet = now();

        if (stub) {
            return ok(engangsstønad);
        }

        LOG.info("Mottak URL: " + mottakServiceUrl);
        template.postForEntity(mottakServiceUrl, body(engangsstønad), String.class);
        return ok(engangsstønad);
    }

    private HttpEntity<EngangsstønadDto> body(@RequestBody Engangsstønad engangsstønad) throws Exception {
        EngangsstønadDto dto = new EngangsstønadDto(engangsstønad);
        String json = new ObjectMapper().writeValueAsString(dto);
        LOG.info("Posting JSON: {}", json);
        return new HttpEntity<>(dto);
    }

    private static URI mottakUriFra(URI baseUri) {
        return UriComponentsBuilder
                .fromUri(baseUri)
                .path("/mottak/dokmot/send")
                .build().toUri();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [template=" + template + ", mottakServiceUrl=" + mottakServiceUrl + "]";
    }
}
