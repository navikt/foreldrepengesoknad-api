package no.nav.foreldrepenger.selvbetjening.rest;

import no.nav.foreldrepenger.selvbetjening.consumer.Oppslagstjeneste;
import no.nav.foreldrepenger.selvbetjening.rest.json.Person;
import no.nav.security.spring.oidc.validation.api.Protected;
import no.nav.security.spring.oidc.validation.api.Unprotected;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static no.nav.foreldrepenger.selvbetjening.rest.OppslagController.REST_OPPSLAG;
import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(REST_OPPSLAG)
@CrossOrigin(allowCredentials = "true")
public class OppslagController {

    public static final String REST_OPPSLAG = "/rest/personinfo";

    private static final Logger LOG = getLogger(OppslagController.class);

    private Oppslagstjeneste oppslag;

    @Inject
    public OppslagController(Oppslagstjeneste oppslag) {
        this.oppslag = oppslag;
    }

    @Protected
    @GetMapping
    public Person personinfo(@RequestParam("fnr") String fnr) {
        LOG.info("Henter personinfo...");
        return new Person(oppslag.hentPerson(fnr));
    }

    // TODO: REMOVE
    @Unprotected
    @GetMapping("/cookie")
    public String testCookie(HttpServletResponse response) {
        response.addCookie(createCookie("tommiscookiejoint", "burger"));
        return "respons";
    }

    private Cookie createCookie(String cookieName, String content) {
        Cookie cookie = new Cookie(cookieName, content);
        if(content == null){
            cookie.setMaxAge(0);
        }
        cookie.setDomain("nais.oera-q.local");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        return cookie;
    }
}

