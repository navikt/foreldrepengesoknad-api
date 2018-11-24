package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import static no.nav.foreldrepenger.selvbetjening.felles.Constants.ISSUER;
import static no.nav.foreldrepenger.selvbetjening.felles.util.EnvUtil.CONFIDENTIAL;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@RequestMapping(path = InnsynController.INNSYN, produces = APPLICATION_JSON_VALUE)
@ProtectedWithClaims(issuer = ISSUER, claimMap = { "acr=Level4" })
public class InnsynController {

    public static final String INNSYN = "/innsyn";

    private static final Logger LOG = getLogger(InnsynController.class);

    private final Innsyn innsyn;

    @Inject
    public InnsynController(Innsyn innsyn) {
        this.innsyn = innsyn;
    }

    @GetMapping("/saker")
    public List<Sak> saker() {
        LOG.info("Henter saker...");
        List<Sak> saker = innsyn.hentSaker();
        LOG.info(CONFIDENTIAL, "Fikk {} sak(er) {}", saker.size(), saker);
        return saker;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [innsyn=" + innsyn + "]";
    }
}
