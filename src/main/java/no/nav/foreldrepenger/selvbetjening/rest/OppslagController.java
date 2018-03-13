package no.nav.foreldrepenger.selvbetjening.rest;

import no.nav.foreldrepenger.selvbetjening.consumer.Oppslagstjeneste;
import no.nav.foreldrepenger.selvbetjening.rest.json.Person;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import static no.nav.foreldrepenger.selvbetjening.rest.OppslagController.REST_OPPSLAG;
import static org.slf4j.LoggerFactory.getLogger;

@CrossOrigin
@RestController
@RequestMapping(REST_OPPSLAG)
public class OppslagController {

    public static final String REST_OPPSLAG = "/rest/personinfo";

    private static final Logger LOG = getLogger(OppslagController.class);

    private Oppslagstjeneste oppslag;

    @Inject
    public OppslagController(Oppslagstjeneste oppslag) {
        this.oppslag = oppslag;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Person personinfo(@RequestParam("fnr") String fnr) {
        LOG.info("Henter personinfo...");
        return new Person(oppslag.hentPerson(fnr));
    }
}
