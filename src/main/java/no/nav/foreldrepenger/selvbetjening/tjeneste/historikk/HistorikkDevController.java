package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import static no.nav.foreldrepenger.selvbetjening.tjeneste.historikk.HistorikkController.HISTORIKK;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.DEV_GCP;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.LOCAL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Fødselsnummer;
import no.nav.security.token.support.core.api.Unprotected;

@Profile({ LOCAL, DEV, DEV_GCP })
@RestController
@RequestMapping(path = HistorikkDevController.DEVPATH, produces = APPLICATION_JSON_VALUE)
@Unprotected
public class HistorikkDevController {

    static final String DEVPATH = HISTORIKK + "/" + DEV;

    private final Historikk historikk;

    public HistorikkDevController(Historikk historikk) {
        this.historikk = historikk;
    }

    @GetMapping
    public List<HistorikkInnslag> historikk(@RequestParam("fnr") Fødselsnummer fnr) {
        return historikk.hentHistorikkFor(fnr);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[historikk=" + historikk + "]";
    }
}
