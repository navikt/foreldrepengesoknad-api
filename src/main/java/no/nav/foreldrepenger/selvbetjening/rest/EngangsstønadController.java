package no.nav.foreldrepenger.selvbetjening.rest;

import static java.time.LocalDateTime.now;
import static no.nav.foreldrepenger.selvbetjening.rest.EngangsstønadController.REST_ENGANGSSTONAD;
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
import no.nav.foreldrepenger.selvbetjening.consumer.json.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.rest.json.Engangsstønad;

@CrossOrigin
@RestController
@RequestMapping(REST_ENGANGSSTONAD)
public class EngangsstønadController {

    public static final String REST_ENGANGSSTONAD = "/rest/engangsstonad";

    private static final Logger LOG = getLogger(EngangsstønadController.class);

    private final RestTemplate template;

    private final URI mottakServiceUrl;

    private final MeterRegistry registry;

    public EngangsstønadController(RestTemplate template, @Value("${FPSOKNAD_MOTTAK_API_URL}") URI baseURI, MeterRegistry registry) {
        this.template = template;
        this.mottakServiceUrl = mottakURIFra(baseURI);
        this.registry = registry;
    }

    @GetMapping("/{id}")
    public Engangsstønad hentEngangsstonad(@PathVariable String id) {
        return Engangsstønad.stub();
    }

    @PostMapping
    public ResponseEntity<Engangsstønad> opprettEngangsstonad(@RequestBody Engangsstønad engangsstønad,
                                                              @RequestParam(name = "stub", defaultValue = "false", required = false) Boolean stub) {
        LOG.info("Poster engangsstønad {}", stub ? "(stub)" : "");

        engangsstønad.opprettet = now();

        if (stub) {
            return ok(engangsstønad);
        }

        LOG.info("Mottak URL: " + mottakServiceUrl);
        template.postForEntity(mottakServiceUrl, new HttpEntity<>(new EngangsstønadDto(engangsstønad)), String.class);
        return ok(engangsstønad);
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
