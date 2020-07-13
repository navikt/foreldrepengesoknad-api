package no.nav.foreldrepenger.selvbetjening.oppslag;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Person;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Søkerinfo;
import no.nav.security.token.support.core.api.ProtectedWithClaims;

@RestController
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
@RequestMapping(OppslagController.OPPSLAG)
public class OppslagController {

    public static final String OPPSLAG = "/rest";

    private final Oppslag oppslag;

    @Inject
    public OppslagController(Oppslag oppslag) {
        this.oppslag = oppslag;
    }

    @GetMapping("/personinfo")
    public Person personinfo() {
        return oppslag.hentPerson();
    }

    @GetMapping("/sokerinfo")
    public Søkerinfo søkerinfo() {
        return oppslag.hentSøkerinfo();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [oppslag=" + oppslag + "]";
    }
}
