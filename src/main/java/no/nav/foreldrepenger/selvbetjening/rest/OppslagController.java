package no.nav.foreldrepenger.selvbetjening.rest;

import static no.nav.foreldrepenger.selvbetjening.rest.OppslagController.REST_OPPSLAG;
import static org.slf4j.LoggerFactory.getLogger;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.selvbetjening.consumer.Oppslag;
import no.nav.foreldrepenger.selvbetjening.rest.json.Person;
import no.nav.security.spring.oidc.validation.api.ProtectedWithClaims;

@RestController
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
@RequestMapping(REST_OPPSLAG)
public class OppslagController {

    public static final String REST_OPPSLAG = "/rest/personinfo";

    private static final Logger LOG = getLogger(OppslagController.class);

    private final Oppslag oppslag;

    @Inject
    public OppslagController(Oppslag oppslag) {
        this.oppslag = oppslag;
    }

    @GetMapping
    public Person personinfo() {
        LOG.info("Henter personinfo...");
        return new Person(oppslag.hentPerson());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [oppslag=" + oppslag + "]";
    }
}
