package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.ISSUER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.security.token.support.core.api.ProtectedWithClaims;

@RestController
@RequestMapping(path = HistorikkController.HISTORIKK, produces = APPLICATION_JSON_VALUE)
@ProtectedWithClaims(issuer = ISSUER, claimMap = { "acr=Level4" })
public class HistorikkController {

    static final String HISTORIKK = "/rest/historikk";

    private final Historikk historikk;

    public HistorikkController(Historikk historikk) {
        this.historikk = historikk;
    }

    @GetMapping
    public List<HistorikkInnslag> historikk() {
        return historikk.historikk();
    }

    @GetMapping(path = "/vedlegg")
    public List<String> vedlegg(@RequestParam("saksnummer") String saksnummer) {
        return historikk.manglendeVedlegg(saksnummer);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [historikk=" + historikk + "]";
    }
}
