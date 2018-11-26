package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag;

import static no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.InnsynController.INNSYN;
import static no.nav.foreldrepenger.selvbetjening.util.Constants.ISSUER;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.CONFIDENTIAL;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.MOVED_PERMANENTLY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.Sak;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Person;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Søkerinfo;
import no.nav.security.oidc.api.ProtectedWithClaims;
import no.nav.security.oidc.api.Unprotected;

@RestController
@RequestMapping(path = OppslagController.OPPSLAG, produces = APPLICATION_JSON_VALUE)
@ProtectedWithClaims(issuer = ISSUER, claimMap = { "acr=Level4" })
public class OppslagController {

    public static final String OPPSLAG = "/rest";

    private static final Logger LOG = getLogger(OppslagController.class);

    private final Oppslag oppslag;

    @Inject
    public OppslagController(Oppslag oppslag) {
        this.oppslag = oppslag;
    }

    @GetMapping("/personinfo")
    public Person personinfo() {
        LOG.info("Henter personinfo...");
        Person person = oppslag.hentPerson();
        LOG.info(CONFIDENTIAL, "Fikk person {}", person);
        return person;
    }

    @GetMapping("/sokerinfo")
    public Søkerinfo søkerinfo() {
        LOG.info("Henter søkerinfo...");
        Søkerinfo info = oppslag.hentSøkerinfo();
        LOG.info(CONFIDENTIAL, "Fikk søkerinfo {}", info);
        return info;
    }

    @GetMapping("/saker")
    @Unprotected
    public ResponseEntity<List<Sak>> saker() {
        LOG.warn("Redirigerer saksoppslag, klienten bør oppdateres");
        return ResponseEntity
                .status(MOVED_PERMANENTLY)
                .header(LOCATION, INNSYN + "/saker")
                .build();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [oppslag=" + oppslag + "]";
    }

}
