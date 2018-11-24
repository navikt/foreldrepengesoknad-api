package no.nav.foreldrepenger.selvbetjening.oppslag;

import static no.nav.foreldrepenger.selvbetjening.felles.util.EnvUtil.CONFIDENTIAL;
import static no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.innsyn.InnsynController.INNSYN;
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

import no.nav.foreldrepenger.selvbetjening.oppslag.json.Person;
import no.nav.foreldrepenger.selvbetjening.oppslag.json.Sak;
import no.nav.foreldrepenger.selvbetjening.oppslag.json.Søkerinfo;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.Oppslag;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.PersonDto;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.SøkerinfoDto;
import no.nav.security.oidc.api.ProtectedWithClaims;
import no.nav.security.oidc.api.Unprotected;

@RestController
@RequestMapping(path = OppslagController.OPPSLAG, produces = APPLICATION_JSON_VALUE)
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
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
        PersonDto person = oppslag.hentPerson();
        LOG.info(CONFIDENTIAL, "Fikk personInfo {}", person);
        return new Person(person);
    }

    @GetMapping("/sokerinfo")
    public Søkerinfo søkerinfo() {
        LOG.info("Henter søkerinfo...");
        SøkerinfoDto info = oppslag.hentSøkerinfo();
        LOG.info(CONFIDENTIAL, "Fikk søkerinfo {}", info);
        return new Søkerinfo(info);
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
