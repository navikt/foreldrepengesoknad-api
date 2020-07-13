package no.nav.foreldrepenger.selvbetjening.historikk;

import static no.nav.foreldrepenger.selvbetjening.historikk.HistorikkController.HISTORIKK;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Fødselsnummer;
import no.nav.security.token.support.core.api.Unprotected;

@RequestMapping(HistorikkDevController.DEVPATH)
@RestController
@Unprotected
@ConditionalOnNotProd
public class HistorikkDevController {

    static final String DEVPATH = HISTORIKK + "/dev";

    private final Historikk historikk;

    public HistorikkDevController(Historikk historikk) {
        this.historikk = historikk;
    }

    @GetMapping
    public List<HistorikkInnslag> historikk(@RequestParam("fnr") Fødselsnummer fnr) {
        return historikk.historikkFor(fnr);
    }

    @GetMapping(path = "/vedlegg")
    public List<String> vedlegg(@RequestParam("fnr") Fødselsnummer fnr, @RequestParam("saksnummer") String saksnummer) {
        return historikk.manglendeVedleggFor(fnr, saksnummer);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[historikk=" + historikk + "]";
    }
}
