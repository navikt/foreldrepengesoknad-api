package no.nav.foreldrepenger.selvbetjening.rest;

import no.nav.foreldrepenger.selvbetjening.consumer.json.PersonDto;
import no.nav.foreldrepenger.selvbetjening.rest.json.Person;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static org.slf4j.LoggerFactory.getLogger;

@CrossOrigin
@RestController
public class PersonController {

    private static final Logger LOG = getLogger(PersonController.class);

    @Value("${FPSOKNAD_OPPSLAG_APIGW_URL}")
    private String oppslagServiceUrl;

    @GetMapping("/personinfo")
    public Person personinfo(@RequestParam("fnr") String fnr, @RequestParam(name = "stub", defaultValue = "false", required = false) Boolean stub) {
        LOG.info("Henter personinfo {}", stub ? "(stub)" : "");
        if (stub) {
            return new Person("Test", "Testesen", "Lyckliga gatan 1A, 0666 Oslo, Norge");
        }

        LOG.info("Oppslag URL: " + oppslagServiceUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-nav-apiKey", "foreldrepengesoknad-api+generertKey");
        HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<PersonDto> response = new RestTemplate().exchange(oppslagServiceUrl + "/person/?fnr=" + fnr, HttpMethod.GET, entity, PersonDto.class);

        return new Person(response.getBody());
    }

}
