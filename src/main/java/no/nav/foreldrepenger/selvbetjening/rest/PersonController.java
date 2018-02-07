package no.nav.foreldrepenger.selvbetjening.rest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import no.nav.foreldrepenger.selvbetjening.consumer.json.PersonDto;
import no.nav.foreldrepenger.selvbetjening.rest.json.Person;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static java.time.LocalDate.now;
import static org.slf4j.LoggerFactory.getLogger;

@CrossOrigin
@RestController
public class PersonController {

    private static final Logger LOG = getLogger(PersonController.class);

    @Value("${FPSOKNAD_OPPSLAG_API_URL}")
    private String oppslagServiceUrl;

    @Value("${FORELDREPENGESOKNAD_API_FPSOKNAD_OPPSLAG_API_APIKEY_PASSWORD}")
    private String apiGatewayKey;

    @Autowired
    private MeterRegistry registry;

    @GetMapping("/personinfo")
    public Person personinfo(@RequestParam("fnr") String fnr, @RequestParam(name = "stub", defaultValue = "false", required = false) Boolean stub) {
        LOG.info("Henter personinfo {}", stub ? "(stub)" : "");

        Counter counter = registry.counter("foreldrepengesoknad.hentet.personinfo");
        counter.increment();

        if (stub) {
            return new Person("Gro Harlem", "Stubberud", "K", now().minusYears(20), "Lyckliga gatan 1A, 0666 Oslo, Norge");
        }

        LOG.info("Oppslag URL: " + oppslagServiceUrl);

        HttpEntity<PersonDto> response = new RestTemplate().exchange(oppslagServiceUrl + "/person/?fnr=" + fnr, HttpMethod.GET, entityWithHeaders(), PersonDto.class);

        return new Person(response.getBody());
    }

    private HttpEntity<?> entityWithHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-nav-apiKey", apiGatewayKey);
        return new HttpEntity<>(headers);
    }

}
