package no.nav.foreldrepenger.selvbetjening.rest;

import no.nav.foreldrepenger.selvbetjening.rest.json.Person;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@CrossOrigin
@RestController
public class PersonController {

    private static final Logger LOG = getLogger(PersonController.class);

    @RequestMapping(method = {GET}, value = "/rest/personinfo")
    public Person personinfo() {
        LOG.info("Henter personinfo (stub)");

        return new Person("Test", "Testesen", "Lyckliga gatan 1A, 0666 Oslo, Norge");
    }

}
