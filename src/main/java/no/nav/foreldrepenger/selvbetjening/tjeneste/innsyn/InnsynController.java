package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.ISSUER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker.Sak;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.uttaksplan.UttaksPeriode;
import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@RequestMapping(path = InnsynController.INNSYN, produces = APPLICATION_JSON_VALUE)
@ProtectedWithClaims(issuer = ISSUER, claimMap = { "acr=Level4" })
public class InnsynController {

    public static final String INNSYN = "/rest/innsyn";

    private final Innsyn innsyn;

    @Inject
    public InnsynController(Innsyn innsyn) {
        this.innsyn = innsyn;
    }

    @GetMapping("/saker")
    public List<Sak> saker() {
        return innsyn.hentSaker();
    }

    @GetMapping(value = "/uttaksplan")
    public List<UttaksPeriode> uttaksplan(@RequestParam(name = "saksnummer") String saksnummer) {
        return innsyn.hentUttaksplan(saksnummer);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [innsyn=" + innsyn + "]";
    }
}
