package no.nav.foreldrepenger.selvbetjening.tjeneste.log;

import no.nav.security.oidc.api.ProtectedWithClaims;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.ISSUER;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@ProtectedWithClaims(issuer = ISSUER, claimMap = { "acr=Level4" })
@RequestMapping(LogController.REST_LOG)
public class LogController {

    public static final String REST_LOG = "/rest/log";
    private static final Logger log = getLogger(LogController.class);

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public void log(@RequestBody String error) {
        log.warn("Frontend:" + error);
    }
}


