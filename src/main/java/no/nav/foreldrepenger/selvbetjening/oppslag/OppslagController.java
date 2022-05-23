package no.nav.foreldrepenger.selvbetjening.oppslag;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

import no.nav.foreldrepenger.common.util.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.PersonFrontend;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Søkerinfo;
import no.nav.security.token.support.core.api.Unprotected;

@ProtectedRestController(OppslagController.OPPSLAG_PATH)
public class OppslagController {

    public static final String OPPSLAG_PATH = "/rest";
    private static final Logger LOG = LoggerFactory.getLogger(OppslagController.class);

    private final Oppslag oppslag;
    private final TokenUtil tokenUtil;

    @Inject
    public OppslagController(Oppslag oppslag, TokenUtil tokenUtil) {
        this.oppslag = oppslag;
        this.tokenUtil = tokenUtil;
    }

    @GetMapping("/personinfo")
    public PersonFrontend personinfo() {
        LOG.trace("Level er {}", tokenUtil.getLevel());
        return oppslag.hentPerson();
    }

    @GetMapping("/sokerinfo")
    public Søkerinfo søkerinfo() {
        LOG.trace("Level er {}", tokenUtil.getLevel());
        return oppslag.hentSøkerinfo();
    }

    @GetMapping("/ping")
    @Unprotected
    public String ping() {
        return oppslag.ping();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [oppslag=" + oppslag + "]";
    }
}
