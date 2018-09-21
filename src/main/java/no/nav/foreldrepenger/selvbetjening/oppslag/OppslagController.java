package no.nav.foreldrepenger.selvbetjening.oppslag;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.selvbetjening.oppslag.json.Person;
import no.nav.foreldrepenger.selvbetjening.oppslag.json.Søkerinfo;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.Oppslag;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.Sak;
import no.nav.security.spring.oidc.validation.api.ProtectedWithClaims;

@RestController
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class OppslagController {

    public static final String REST_PERSONINFO = "/rest/personinfo";
    private static final String REST_SØKERINFO = "/rest/sokerinfo";
    private static final String REST_SAKER = "/rest/saker";
    private static final String REST_SØKNADER = "/rest/soknader";

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

    @GetMapping(REST_SAKER)
    public List<Sak> saker() {
        LOG.info("Henter saker...");
        return oppslag.hentSaker();
    }

    @GetMapping(REST_SØKNADER)
    public String søknad() {
        throw new NotImplementedException("Uthenting av søknad ikke implementert");
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [oppslag=" + oppslag + "]";
    }

}
