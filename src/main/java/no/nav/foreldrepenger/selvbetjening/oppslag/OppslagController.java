package no.nav.foreldrepenger.selvbetjening.oppslag;

import no.nav.foreldrepenger.selvbetjening.oppslag.json.Person;
import no.nav.foreldrepenger.selvbetjening.oppslag.json.Søkerinfo;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.Oppslag;
import no.nav.security.spring.oidc.validation.api.ProtectedWithClaims;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class OppslagController {

    public static final String REST_PERSONINFO = "/rest/personinfo";
    private static final String REST_SØKERINFO = "/rest/sokerinfo";

    private static final Logger LOG = getLogger(OppslagController.class);

    private final Oppslag oppslag;

    @Inject
    public OppslagController(Oppslag oppslag) {
        this.oppslag = oppslag;
    }

    @GetMapping(REST_PERSONINFO)
    public Person personinfo() {
        LOG.info("Henter personinfo...");
        return new Person(oppslag.hentPerson());
    }

    @GetMapping(REST_SØKERINFO)
    public Søkerinfo søkerinfo() {
        LOG.info("Henter søkerinfo...");
        return new Søkerinfo(oppslag.hentSøkerinfo());
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + " [oppslag=" + oppslag + "]";
    }

}
