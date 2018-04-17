package no.nav.foreldrepenger.selvbetjening.rest;

import static no.nav.foreldrepenger.selvbetjening.rest.OppslagController.REST_OPPSLAG;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import javax.inject.Inject;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.selvbetjening.consumer.Oppslag;
import no.nav.foreldrepenger.selvbetjening.rest.json.Person;
import no.nav.security.spring.oidc.validation.api.ProtectedWithClaims;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
@RequestMapping(REST_OPPSLAG)
public class OppslagController {

    public static final String REST_OPPSLAG = "/rest/personinfo";

    private static final Logger LOG = getLogger(OppslagController.class);

    private final Oppslag oppslag;

    private final Counter notFoundCounter = Metrics.counter("fpsoknad.api.person.notfound");

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

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleException(HttpClientErrorException ex) {
        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
            notFoundCounter.increment();
            LOG.warn("Got 404 from oppslag, is the gateway down?");
            return new ResponseEntity("Nope, could't find that", NOT_FOUND);
        }
        throw ex;
    }
}
