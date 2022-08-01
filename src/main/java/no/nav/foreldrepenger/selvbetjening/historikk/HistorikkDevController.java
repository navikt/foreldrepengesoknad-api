package no.nav.foreldrepenger.selvbetjening.historikk;

import static no.nav.foreldrepenger.common.util.Constants.FNR;
import static no.nav.foreldrepenger.selvbetjening.historikk.HistorikkController.HISTORIKK;

import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.http.UnprotectedRestController;

@Validated
@UnprotectedRestController(HistorikkDevController.DEVPATH)
public class HistorikkDevController {

    static final String DEVPATH = HISTORIKK + "/dev";

    private final Historikk historikk;

    public HistorikkDevController(Historikk historikk) {
        this.historikk = historikk;
    }

    @GetMapping
    public List<HistorikkInnslag> historikk(@Valid @RequestParam(FNR) Fødselsnummer fnr) {
        return historikk.historikkFor(fnr);
    }

    @GetMapping(path = "/vedlegg")
    public List<String> vedlegg(@Valid @RequestParam(FNR) Fødselsnummer fnr, @Valid @RequestParam("saksnummer") Saksnummer saksnummer) {
        return historikk.manglendeVedleggFor(fnr, saksnummer);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[historikk=" + historikk + "]";
    }
}
