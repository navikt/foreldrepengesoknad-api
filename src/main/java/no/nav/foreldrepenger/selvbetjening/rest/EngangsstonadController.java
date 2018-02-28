package no.nav.foreldrepenger.selvbetjening.rest;

import static java.time.LocalDateTime.now;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.ok;

import java.net.URI;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
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
import org.springframework.web.util.UriComponentsBuilder;

import io.micrometer.core.instrument.MeterRegistry;
import no.nav.foreldrepenger.selvbetjening.consumer.json.EngangsstonadDto;
import no.nav.foreldrepenger.selvbetjening.rest.json.Engangsstonad;

@CrossOrigin
@RestController
@RequestMapping(EngangsstonadController.REST_ENGANGSSTONAD)
public class EngangsstonadController {

    public static final String REST_ENGANGSSTONAD = "/rest/engangsstonad";

    private static final Logger LOG = getLogger(EngangsstonadController.class);

    private final RestTemplate template;

    private final URI mottakServiceUrl;

    private final MeterRegistry registry;

    public EngangsstonadController(RestTemplate template,
            @Value("${FPSOKNAD_MOTTAK_API_URL}") URI baseURI, MeterRegistry registry) {
        this.template = template;
        this.mottakServiceUrl = mottakURIFra(baseURI);
        this.registry = registry;
    }

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
            engangsstonad.id = "42";
            return ok(engangsstonad);
        }
        LOG.info("Mottak URL: " + mottakServiceUrl);
        template.postForEntity(mottakServiceUrl, new HttpEntity<>(new EngangsstonadDto(engangsstonad)), String.class);
        return ok(engangsstonad);
    }

    private static URI mottakURIFra(URI baseUri) {
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
