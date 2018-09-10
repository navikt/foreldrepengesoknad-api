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
        try {

            if (EnvUtil.isDevOrPreprod(env)) {
                LOG.info("Henter søknad...");
                oppslag.hentSøknad("1026787");
                LOG.info("Henter saker...");
                List<Sak> fagsaker = oppslag.hentSaker();
                LOG.info("Fagsaker {}", fagsaker);

                /*
                 * for (Fagsak fagsak : fagsaker) { for (Behandling behandling :
                 * fagsak.getBehandlinger()) { LOG.info("Henter søknad for {} {}",
                 * fagsak.getSaksnummer(), behandling.getId()); // LOG.info("{}",
                 * oppslag.hentSøknad(behandling.getId())); } }
                 */
            }
        } catch (Exception e) {
            LOG.warn("Oops", e);
        }
        return new Søkerinfo(oppslag.hentSøkerinfo());
    }

    @GetMapping(REST_SAKER)
    public List<Sak> saker() {
        LOG.info("Henter saker...");
        return oppslag.hentSaker();
    }

    @GetMapping(REST_SØKNADER)
    public String søknad() {
        try {
            LOG.info("Henter søknad...");
            return oppslag.hentSøknad("1000525");
        } catch (Exception e) {
            LOG.warn("Oops", e);
            return null;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [oppslag=" + oppslag + "]";
    }

}
