package no.nav.foreldrepenger.selvbetjening.sats;


import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.security.token.support.core.api.Unprotected;

@Validated
@Unprotected
@RestController
@RequestMapping(SatsController.SATS_PATH)
public class SatsController {

    static final String SATS_PATH = "/rest/satser";

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*", allowCredentials = "false")
    public String satser() {
        return Satser.SATSER;
    }






}
