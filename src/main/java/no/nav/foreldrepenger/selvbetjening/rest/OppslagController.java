package no.nav.foreldrepenger.selvbetjening.rest;

import static java.time.LocalDate.now;
import static org.slf4j.LoggerFactory.getLogger;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.micrometer.core.instrument.MeterRegistry;
import no.nav.foreldrepenger.selvbetjening.consumer.json.PersonDto;
import no.nav.foreldrepenger.selvbetjening.rest.json.Person;

@CrossOrigin
@RestController
@RequestMapping(OppslagController.REST_OPPSLAG)
public class OppslagController {

    public static final String REST_OPPSLAG = "/rest/personinfo";

    private static final Logger LOG = getLogger(OppslagController.class);

    private final String oppslagServiceUrl;

    private final RestTemplate template;

    private final MeterRegistry registry;

    @Inject
    public OppslagController(RestTemplate template, @Value("${FPSOKNAD_OPPSLAG_API_URL}") String uri,
            MeterRegistry registry) {
        this.template = template;
        this.oppslagServiceUrl = uri;
        this.registry = registry;
    }

    public Person personinfo(@RequestParam("fnr") String fnr,
            @RequestParam(name = "stub", defaultValue = "false", required = false) Boolean stub) {
        LOG.info("Henter personinfo {}", stub ? "(stub)" : "");

        registry.counter("foreldrepengesoknad.hentet.personinfo").increment();
        if (stub) {
            return new Person("Gro Harlem", "Stubberud", "K", now().minusYears(20),
                    "Lyckliga gatan 1A, 0666 Oslo, Norge");
        }

        LOG.info("Oppslag URL: " + oppslagServiceUrl);
        String url = oppslagServiceUrl + "/person/?fnr=" + fnr;
        LOG.info("Slår opp i {}", url);
        return new Person(template.getForObject(url, PersonDto.class));
    }
}
