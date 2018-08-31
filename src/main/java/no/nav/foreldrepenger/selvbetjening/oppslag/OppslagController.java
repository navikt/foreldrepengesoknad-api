package no.nav.foreldrepenger.selvbetjening.oppslag;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.selvbetjening.felles.util.EnvUtil;
import no.nav.foreldrepenger.selvbetjening.oppslag.json.Person;
import no.nav.foreldrepenger.selvbetjening.oppslag.json.Søkerinfo;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.Oppslag;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.Fagsak;
import no.nav.security.spring.oidc.validation.api.ProtectedWithClaims;

@RestController
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class OppslagController {

    public static final String REST_PERSONINFO = "/rest/personinfo";
    private static final String REST_SØKERINFO = "/rest/sokerinfo";
    private static final String REST_FAGSAKER = "/rest/fagsaker";
    private static final String REST_SØKNADER = "/rest/soknader";

    private static final Logger LOG = getLogger(OppslagController.class);

    @Inject
    private Environment env;
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

        if (EnvUtil.isDevOrPreprod(env)) {
            LOG.info("{}", søknad());
        }

        return new Søkerinfo(oppslag.hentSøkerinfo());
    }

    @GetMapping(REST_FAGSAKER)
    public List<Fagsak> fagsaker() {
        LOG.info("Henter fagsaker...");
        return oppslag.hentFagsaker();
    }

    @GetMapping(REST_SØKNADER)
    public String søknad() {
        LOG.info("Henter søknad...");
        return oppslag.hentSøknad("1000525");
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [oppslag=" + oppslag + "]";
    }

}
