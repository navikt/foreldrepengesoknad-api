package no.nav.foreldrepenger.selvbetjening.oppslag;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;

import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Person;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Søkerinfo;

@ProtectedRestController(OppslagController.OPPSLAG)
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

    @GetMapping("/ping")
    public String ping() {
        return oppslag.ping();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [oppslag=" + oppslag + "]";
    }
}
