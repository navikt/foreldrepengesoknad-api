package no.nav.foreldrepenger.selvbetjening.rest;

import no.nav.foreldrepenger.selvbetjening.consumer.Oppslagstjeneste;
import no.nav.foreldrepenger.selvbetjening.rest.json.Person;
import no.nav.security.spring.oidc.validation.api.Protected;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import static no.nav.foreldrepenger.selvbetjening.rest.OppslagController.REST_OPPSLAG;
import static org.slf4j.LoggerFactory.getLogger;

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

    @Protected
    @GetMapping
    public Person personinfo(@RequestParam("fnr") String fnr) {
        LOG.info("Henter personinfo...");
        return new Person(oppslag.hentPerson(fnr));
    }
}
